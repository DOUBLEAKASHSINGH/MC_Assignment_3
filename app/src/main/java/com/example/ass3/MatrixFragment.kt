package com.example.mc_assignment_3

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.example.mc_assignment_3.databinding.FragmentMatrixBinding

class MatrixFragment : Fragment() {
    private var _binding: FragmentMatrixBinding? = null
    private val binding get() = _binding!!

    private var rows = 2
    private var cols = 2
    private val maxSize = 10
    private val cells = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatrixBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spinner setup for selecting rows/cols
        val sizes = (1..maxSize).toList()
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sizes
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerRows.adapter = spinnerAdapter
        binding.spinnerCols.adapter = spinnerAdapter
        binding.spinnerRows.setSelection(rows - 1)
        binding.spinnerCols.setSelection(cols - 1)

        // Reset clears all cells; Resize rebuilds the grid
        binding.btnReset.setOnClickListener { cells.forEach { it.setText("") } }
        binding.btnResize.setOnClickListener {
            rows = binding.spinnerRows.selectedItem as Int
            cols = binding.spinnerCols.selectedItem as Int
            makeGrid()
        }

        // Build initial 2×2 grid
        makeGrid()
    }

    private fun makeGrid() {
        binding.gridMatrix.removeAllViews()
        cells.clear()

        // Inform GridLayout exactly how many rows & columns
        binding.gridMatrix.rowCount = rows
        binding.gridMatrix.columnCount = cols

        // Place each EditText at explicit (row, col)
        for (i in 0 until rows * cols) {
            val row = i / cols
            val col = i % cols

            val params = GridLayout.LayoutParams(
                /* rowSpec    = */ GridLayout.spec(row, 1, 1f),
                /* columnSpec = */ GridLayout.spec(col, 1, 1f)
            ).apply {
                width  = 0
                height = WRAP_CONTENT
            }

            val edit = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_SIGNED or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
                layoutParams = params
                setPadding(8, 8, 8, 8)
                hint = ""  // you can put "[$row,$col]" here if you like
            }

            cells += edit
            binding.gridMatrix.addView(edit)
        }
    }

    /**
     * Called by ResultFragment to retrieve the user’s matrix.
     * Returns Pair(rows, Array<DoubleArray>) so we know dimensions.
     */
    fun getMatrix(): Pair<Int, Array<DoubleArray>> {
        val mat = Array(rows) { r ->
            DoubleArray(cols) { c ->
                cells[r * cols + c]
                    .text
                    .toString()
                    .toDoubleOrNull()
                    ?: 0.0
            }
        }
        return rows to mat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
