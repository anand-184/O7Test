package com.anand.o7test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.anand.o7test.databinding.FragmentBookingScreenBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookingScreen : Fragment() {
    private var binding: FragmentBookingScreenBinding? = null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Travel Class Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.travel_class,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.travelClassSpinner?.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.travel_time,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.travelTimeSpinner?.adapter = adapter
        }

        binding?.btnFareCalc?.setOnClickListener {
            calculateFare()
        }

        binding?.btnBook?.setOnClickListener {
            val fare = calculateFare()
                Toast.makeText(requireContext(), "Booking Confirmed! Total Fare: ₹${fare}", Toast.LENGTH_LONG
                ).show()

        }
    }

    private fun calculateFare(): Double {
        try {

            val passengerName = binding?.etPassengerName?.text.toString()
            val ageText = binding?.etAge?.text.toString()
            val ticketsText = binding?.etNoOfTickets?.text.toString()
            val distanceText = binding?.etDistance?.text.toString()
            val travelClass = binding?.travelClassSpinner?.selectedItem.toString()
            val travelTime = binding?.travelTimeSpinner?.selectedItem.toString()

            if (passengerName.isEmpty() || ageText.isEmpty() || ticketsText.isEmpty() ||
                distanceText.isEmpty() || travelClass.isEmpty() || travelTime.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return 0.0
            }

            val age = ageText.toInt()
            val noOfTickets = ticketsText.toInt()
            val distance = distanceText.toInt()

            if (age <= 0 || noOfTickets <= 0 || distance <= 0) {
                Toast.makeText(requireContext(), "Please enter valid positive values", Toast.LENGTH_SHORT).show()
                return 0.0
            }

            val ratePerKm = when (travelClass) {
                "Normal" -> 2.0
                "AC" -> 3.0
                "Sleeper" -> 4.0
                "Luxury" -> 6.0
                else -> 2.0
            }
            var farePerTicket = distance * ratePerKm

            val isPeakHour = isPeakHour(travelTime)
            if (isPeakHour) {
                farePerTicket *= 1.20
            }


            farePerTicket = when {
                age < 12 -> farePerTicket * 0.50
                age >= 60 -> farePerTicket * 0.70
                else -> farePerTicket
            }
            var totalFare = farePerTicket * noOfTickets

            if (noOfTickets >= 5) {
                totalFare *= 0.90
            }

            val serviceFee = 50.0
            totalFare += serviceFee

            val gst = totalFare * 0.05
            totalFare += gst



            return totalFare

        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            return 0.0
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            return 0.0
        }
    }

    private fun isPeakHour(travelTime: String): Boolean {

        return when (travelTime.lowercase()) {
            "morning (6am-10am)" -> true
            "evening (6pm-9pm)" -> true
            "peak hours" -> true
            else -> false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
