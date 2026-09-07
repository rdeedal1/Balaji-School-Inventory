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
import com.balaji.school.inventory.data.Product
import com.balaji.school.inventory.databinding.FragmentProductsBinding
import kotlinx.coroutines.launch

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupProductsUI()
    }

    private fun setupProductsUI() {
        val db = AppDatabase.getInstance(requireContext())
        
        binding.addProductBtn.setOnClickListener {
            addProduct(db)
        }
    }

    private fun addProduct(db: AppDatabase) {
        val name = binding.productNameInput.text.toString().trim()
        val price = binding.productPriceInput.text.toString().trim()
        val quantity = binding.productQuantityInput.text.toString().trim()
        
        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(requireContext(), R.string.empty_field, Toast.LENGTH_SHORT).show()
            return
        }
        
        val product = Product(
            name = name,
            price = price.toDouble(),
            quantity = quantity.toInt()
        )
        
        viewLifecycleOwner.lifecycleScope.launch {
            db.productDAO().insert(product)
            Toast.makeText(requireContext(), R.string.product_added, Toast.LENGTH_SHORT).show()
            clearInputs()
        }
    }
    
    private fun clearInputs() {
        binding.productNameInput.text.clear()
        binding.productPriceInput.text.clear()
        binding.productQuantityInput.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
