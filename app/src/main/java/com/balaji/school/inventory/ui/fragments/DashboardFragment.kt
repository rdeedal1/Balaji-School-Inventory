package com.balaji.school.inventory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.balaji.school.inventory.R
import com.balaji.school.inventory.data.AppDatabase
import com.balaji.school.inventory.databinding.FragmentDashboardBinding
import com.balaji.school.inventory.utils.DateUtils
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupDashboard()
    }

    private fun setupDashboard() {
        val db = AppDatabase.getInstance(requireContext())
        val saleDAO = db.saleDAO()
        val productDAO = db.productDAO()
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Today's sales
            val todayStart = DateUtils.getTodayStart()
            val todayEnd = DateUtils.getTodayEnd()
            
            saleDAO.getTotalSalesByDateRange(todayStart, todayEnd).collect { totalSales ->
                binding.todaySalesValue.text = "₹${totalSales ?: 0}"
            }
            
            // Total revenue (all time)
            saleDAO.getAllSales().collect { sales ->
                val total = sales.sumOf { it.totalAmount }
                binding.totalRevenueValue.text = "₹$total"
            }
            
            // Total stock
            productDAO.getAllProducts().collect { products ->
                val totalQty = products.sumOf { it.quantity }
                binding.totalStockValue.text = totalQty.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
