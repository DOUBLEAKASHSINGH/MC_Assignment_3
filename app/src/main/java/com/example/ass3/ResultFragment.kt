package com.example.mc_assignment_3

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mc_assignment_3.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnComputeResult.setOnClickListener { compute() }
        binding.btnResetResult .setOnClickListener { binding.gridResult.removeAllViews() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun compute() {
        // Grab matrices from fragments
        val act = activity as MainActivity
        val (_, A) = act.fragmentA.getMatrix()
        val (_, B) = act.fragmentB.getMatrix()

        // Flatten
        val flatA = A.flatMap { it.toList() }.toDoubleArray()
        val flatB = B.flatMap { it.toList() }.toDoubleArray()
        val count = flatA.size

        // Pick operation
        val op = when (binding.rgOps.checkedRadioButtonId) {
            R.id.rbAdd -> '+'
            R.id.rbSub -> '-'
            R.id.rbMul -> '*'
            R.id.rbDiv -> '/'
            else       -> '+'
        }

        // Native compute
        val flatC = MainActivity.MatrixNative.nativeCompute(count, flatA, flatB, op)

        // Rebuild 2D result
        val rows = A.size
        val cols = if (rows > 0) A[0].size else 0
        val result = Array(rows) { r ->
            DoubleArray(cols) { c ->
                flatC[r * cols + c]
            }
        }

        // Render, forcing integer display if division
        render(rows, cols, result, op == '/')
    }

    private fun render(r: Int, c: Int, M: Array<DoubleArray>, forceInteger: Boolean) {
        binding.gridResult.removeAllViews()
        binding.gridResult.rowCount    = r
        binding.gridResult.columnCount = c

        for (i in 0 until r * c) {
            val row   = i / c
            val col   = i % c
            val value = M[row][col]

            // Determine displayed text
            val text = if (forceInteger) {
                // truncate toward zero
                value.toInt().toString()
            } else if (value % 1.0 == 0.0) {
                // exact integer
                value.toInt().toString()
            } else {
                // non-integer float
                value.toString()
            }

            val params = GridLayout.LayoutParams(
                GridLayout.spec(row, 1, 1f),
                GridLayout.spec(col, 1, 1f)
            ).apply {
                width  = 0
                height = WRAP_CONTENT
            }

            binding.gridResult.addView(TextView(requireContext()).apply {
                this.text         = text
                gravity            = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                layoutParams       = params
            })
        }
    }
}
