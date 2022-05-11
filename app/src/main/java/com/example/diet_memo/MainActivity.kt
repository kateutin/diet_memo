package com.example.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    val dataModelList = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val myRef = database.getReference("myMemo")

        val listView = findViewById<ListView>(R.id.mainLV)

        val adapter_list = ListViewAdapter(dataModelList)

        listView.adapter = adapter_list
        Log.d("DataModel------", dataModelList.toString())

            myRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (dataModel in snapshot.children){
                        Log.d("Data",dataModel.toString())
                        dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
                    }
                }//스냅샷을 반복문을 통해 꺼내준다

                override fun onCancelled(error: DatabaseError) {

                }

            })


        val writeButton =findViewById<ImageView>(R.id.writeBtn) //ImageView에서 id가 writeBtn에를 찾아 writeButton로 변수 선언
        writeButton.setOnClickListener{

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("운동 메모 다이얼로그")

            val mAlertDialog = mBuilder.show()

            val DateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

            var dateText = ""

            DateSelectBtn?.setOnClickListener {

                val today = GregorianCalendar()//캘린더를 가지고 와서
                val year : Int = today.get(Calendar.YEAR)//년
                val month : Int = today.get(Calendar.MONTH)//월
                val date : Int = today.get(Calendar.DATE)//일

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int
                    ) {
                        Log.d("MAIN", "${year}, ${month + 1}, ${dayOfMonth}")//${}는 year.toString()과 동일한 기능 월에는 +1이 꼭 들어가야 한다
                        DateSelectBtn.setText("${year}, ${month + 1}, ${dayOfMonth}")

                        dateText = "${year}, ${month + 1}, ${dayOfMonth}"
                    }

                 }, year, month, date)
                dlg.show()

            }//클릭시 일어나는 이벤트

            val saveBtn = mAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {

                val healMemo = mAlertDialog.findViewById<EditText>(R.id.healthMemo)?.text.toString()

                    val database = Firebase.database
                    val myRef = database.getReference("myMemo").child(Firebase.auth.currentUser!!.uid)//현재 유저의 uid만 넣어준다

                    val model = DataModel(dateText,healMemo)

                    myRef.push().setValue(model)

                    mAlertDialog.dismiss()

                }
        }

    }
}