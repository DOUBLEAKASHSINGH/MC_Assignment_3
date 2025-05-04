#include <jni.h>
#include <valarray>
#include <vector>
#include <cmath>

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_example_mc_1assignment_13_MainActivity_00024MatrixNative_nativeCompute(
        JNIEnv* env,
        jclass /* clazz */,
        jint len,           // total elements = N*N
        jdoubleArray aArr,
        jdoubleArray bArr,
        jchar op) {

    // 1) pull inputs into valarrays
    jdouble* aElems = env->GetDoubleArrayElements(aArr, nullptr);
    jdouble* bElems = env->GetDoubleArrayElements(bArr, nullptr);
    std::valarray<double> A(aElems, len);
    std::valarray<double> B(bElems, len);
    env->ReleaseDoubleArrayElements(aArr, aElems, 0);
    env->ReleaseDoubleArrayElements(bArr, bElems, 0);

    // 2) prepare output container
    std::valarray<double> C(len);

    // dimension N
    int N = static_cast<int>(std::sqrt(len));

    if (op == '*') {
        // true matrix multiply (A × B)
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                double sum = 0.0;
                for (int k = 0; k < N; ++k)
                    sum += A[i*N + k] * B[k*N + j];
                C[i*N + j] = sum;
            }
        }
    }
    else if (op == '/') {
        // 1) build B_mat and identity
        std::vector<std::vector<double>> mat(N, std::vector<double>(N));
        std::vector<std::vector<double>> inv(N, std::vector<double>(N, 0.0));
        for (int i = 0; i < N; ++i) {
            inv[i][i] = 1.0;
            for (int j = 0; j < N; ++j) {
                mat[i][j] = B[i*N + j];
            }
        }
        // 2) Gauss–Jordan to invert B
        for (int i = 0; i < N; ++i) {
            // pivot
            if (std::fabs(mat[i][i]) < 1e-12) {
                // find non-zero below
                int swapRow = i+1;
                while (swapRow < N && std::fabs(mat[swapRow][i]) < 1e-12) ++swapRow;
                if (swapRow == N) return nullptr; // singular: bail
                std::swap(mat[i], mat[swapRow]);
                std::swap(inv[i], inv[swapRow]);
            }
            double piv = mat[i][i];
            // normalize row i
            for (int j = 0; j < N; ++j) {
                mat[i][j] /= piv;
                inv[i][j] /= piv;
            }
            // eliminate other rows
            for (int r = 0; r < N; ++r) {
                if (r == i) continue;
                double factor = mat[r][i];
                for (int c = 0; c < N; ++c) {
                    mat[r][c] -= factor * mat[i][c];
                    inv[r][c] -= factor * inv[i][c];
                }
            }
        }
        // 3) flatten inv into valarray
        std::valarray<double> Binv(len);
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                Binv[i*N + j] = inv[i][j];

        // 4) multiply A × B⁻¹
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                double sum = 0.0;
                for (int k = 0; k < N; ++k)
                    sum += A[i*N + k] * Binv[k*N + j];
                C[i*N + j] = sum;
            }
        }
    }
    else {
        // element-wise +, –, /
        switch (op) {
            case '+': C = A + B; break;
            case '-': C = A - B; break;
            case '/': C = A / B; break; // fallback
            default:  C = A + B; break;
        }
    }

    // 3) copy back
    jdoubleArray result = env->NewDoubleArray(len);
    env->SetDoubleArrayRegion(result, 0, len, &C[0]);
    return result;
}
