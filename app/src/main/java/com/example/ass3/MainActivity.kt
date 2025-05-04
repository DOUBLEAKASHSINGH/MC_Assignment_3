package com.example.mc_assignment_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_assignment_3.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // expose for ResultFragment to pull A & B
    lateinit var fragmentA: MatrixFragment
    lateinit var fragmentB: MatrixFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentA = MatrixFragment()
        fragmentB = MatrixFragment()
        val fragmentResult = ResultFragment()

        val adapter = ViewPagerAdapter(
            this,
            listOf(fragmentA, fragmentB, fragmentResult),
            listOf(
                getString(R.string.tab_a),
                getString(R.string.tab_b),
                getString(R.string.tab_result)
            )
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = adapter.titles[pos]
        }.attach()
    }

    object MatrixNative {
        init { System.loadLibrary("matrixcalc") }
        @JvmStatic external fun nativeCompute(
            n: Int,
            flatA: DoubleArray,
            flatB: DoubleArray,
            op: Char
        ): DoubleArray
    }
}
