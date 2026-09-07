package com.balaji.school.inventory.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.balaji.school.inventory.R
import com.balaji.school.inventory.data.AppDatabase
import com.balaji.school.inventory.databinding.FragmentReportsBinding
import com.balaji.school.inventory.utils.DateUtils
import kotlinx.coroutines.launch

class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupReportsUI()
    }

    private fun setupReportsUI() {
        val db = AppDatabase.getInstance(requireContext())
        
        binding.todayReportBtn.setOnClickListener {
            loadTodayReport(db)
        }
        
        binding.weeklyReportBtn.setOnClickListener {
            loadWeeklyReport(db)
        }
        
        binding.monthlyReportBtn.setOnClickListener {
            loadMonthlyReport(db)
        }
    }
    
    private fun loadTodayReport(db: AppDatabase) {
        val todayStart = DateUtils.getTodayStart()
        val todayEnd = DateUtils.getTodayEnd()
        
        viewLifecycleOwner.lifecycleScope.launch {
            db.saleDAO().getSalesByDateRange(todayStart, todayEnd).collect { sales ->
                val total = sales.sumOf { it.totalAmount }
                binding.reportContent.text = "Today's Sales: ${sales.size}\nTotal: ₹$total"
            }
        }
    }
    
    private fun loadWeeklyReport(db: AppDatabase) {
        val weekStart = DateUtils.getWeekStart()
        val todayEnd = DateUtils.getTodayEnd()
        
        viewLifecycleOwner.lifecycleScope.launch {
            db.saleDAO().getSalesByDateRange(weekStart, todayEnd).collect { sales ->
                val total = sales.sumOf { it.totalAmount }
                binding.reportContent.text = "Weekly Sales: ${sales.size}\nTotal: ₹$total"
            }
        }
    }
    
    private fun loadMonthlyReport(db: AppDatabase) {
        val monthStart = DateUtils.getMonthStart()
        val todayEnd = DateUtils.getTodayEnd()
        
        viewLifecycleOwner.lifecycleScope.launch {
            db.saleDAO().getSalesByDateRange(monthStart, todayEnd).collect { sales ->
                val total = sales.sumOf { it.totalAmount }
                binding.reportContent.text = "Monthly Sales: ${sales.size}\nTotal: ₹$total"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
