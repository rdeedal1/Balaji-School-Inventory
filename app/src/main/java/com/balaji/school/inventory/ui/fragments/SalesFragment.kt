package com.balaji.school.inventory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.balaji.school.inventory.R
import com.balaji.school.inventory.data.AppDatabase
import com.balaji.school.inventory.data.Sale
import com.balaji.school.inventory.databinding.FragmentSalesBinding
import kotlinx.coroutines.launch

class SalesFragment : Fragment(R.layout.fragment_sales) {

    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupSalesUI()
    }

    private fun setupSalesUI() {
        val db = AppDatabase.getInstance(requireContext())
        
        binding.recordSaleBtn.setOnClickListener {
            recordSale(db)
        }
    }

    private fun recordSale(db: AppDatabase) {
        val productId = binding.productIdInput.text.toString().trim().toIntOrNull() ?: return
        val quantity = binding.quantitySoldInput.text.toString().trim().toIntOrNull() ?: return
        val totalAmount = binding.totalAmountInput.text.toString().trim().toDoubleOrNull() ?: return
        
        if (productId <= 0 || quantity <= 0) {
            Toast.makeText(requireContext(), R.string.invalid_quantity, Toast.LENGTH_SHORT).show()
            return
        }
        
        val sale = Sale(
            productId = productId,
            quantitySold = quantity,
            totalAmount = totalAmount
        )
        
        viewLifecycleOwner.lifecycleScope.launch {
            db.saleDAO().insert(sale)
            db.productDAO().decreaseQuantity(productId, quantity)
            Toast.makeText(requireContext(), R.string.sale_recorded, Toast.LENGTH_SHORT).show()
            clearInputs()
        }
    }
    
    private fun clearInputs() {
        binding.productIdInput.text.clear()
        binding.quantitySoldInput.text.clear()
        binding.totalAmountInput.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
