package android.example.onlineordering

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.example.onlineordering.databinding.FragmentDetailBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class DetailFragment : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, BottomSheetDialogFragment() {

    private lateinit var detailBinding: FragmentDetailBinding
    private var arbuz = listOf(
        "Неспелый",
        "Спелый",
        "Уже сорван")
    private var quantity = listOf(1,2,3)
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0
    private val cal  = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailBinding = FragmentDetailBinding.bind(view)
        detailBinding.apply {
            spinner?.adapter = context?.let {
                ArrayAdapter(
                    it,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    quantity
                )
            } as SpinnerAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    tvQuantity.text = quantity.get(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
        randomValue()
        pickDate()

        detailBinding.btnOrder.setOnClickListener {
            if (detailBinding.etAddress.text.isNotEmpty() and detailBinding.etPhoneNumber.text.isNotEmpty()) {
                if (savedDay != 0) {
                    var orderAcceptFragment = OrderAcceptFragment()
                    orderAcceptFragment.show(parentFragmentManager, "OrderAcceptFragment")
                    this.dismiss()
                }
            }
            if (savedDay == 0) {
                Toast.makeText(activity, "Выберите время доставки", Toast.LENGTH_SHORT).show()
            } else if (detailBinding.etAddress.text.isEmpty() or detailBinding.etPhoneNumber.text.isEmpty()) {
                Toast.makeText(activity, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun randomValue(){
        val random = Random()
        val randomIndex = random.nextInt(3)
        val randomWeight = (7..13).random()
        detailBinding.apply{
            tvStatus.text = arbuz[randomIndex]
            tvWeight.text = "$randomWeight кг"
        }
    }

    private fun getDataTimeCalendar(){
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        detailBinding.btnDateTime.setOnClickListener{
            getDataTimeCalendar()
            val dialog = context?.let { itDateTime -> DatePickerDialog(itDateTime, this, year, month, day) }
            dialog?.apply {
                datePicker?.minDate = cal.timeInMillis
                cal.set(year, month, day + 8)
                datePicker?.maxDate = cal.timeInMillis
                show()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2 + 1
        savedYear = p1
        getDataTimeCalendar()
        TimePickerDialog(context, this, hour, minute, true).show()
    }
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2
        detailBinding.apply {
            tvDateTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute: $savedMinute"
        }
    }
}