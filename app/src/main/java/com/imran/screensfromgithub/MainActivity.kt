package com.imran.screensfromgithub


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.imran.screensfromgithub.api.ApiClient
import com.imran.screensfromgithub.api.ApiSet
import com.imran.screensfromgithub.models.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {
    private lateinit var apiSet: ApiSet
    private lateinit var layouts : List<Layout>
    private lateinit var decodedXmlString : String
    private lateinit var code: TextView
    private lateinit var xmlName: TextView
    private lateinit var getFiles : Button
    private lateinit var usernameEt : EditText
    private lateinit var repoEt : EditText
    private lateinit var xmlLayout : LinearLayout
    private var count : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiSet = ApiClient.getClient().create(ApiSet::class.java)


        code = findViewById(R.id.codeTextView)
        xmlName = findViewById(R.id.name)
        getFiles = findViewById(R.id.getFiles)
        usernameEt = findViewById(R.id.username_et)
        repoEt = findViewById(R.id.repo_et)
        xmlLayout = findViewById(R.id.xml_layout)

        getFiles.setOnClickListener {
            val username = usernameEt.text.toString()
            val repo = repoEt.text.toString()

            getAllLayoutFiles(username,repo)
            xmlLayout.visibility = View.VISIBLE
        }



    }

    private fun getAllLayoutFiles(username: String, repo: String) {

        CoroutineScope(Dispatchers.IO).launch {
//            val result =  apiSet.getAllLayoutFiles("imran0developer","Google_map_app")
            val result =  apiSet.getAllLayoutFiles(username,repo)
            if (result.isSuccessful){
                val layoutsData = result.body()

                layouts = layoutsData?.map { layout ->
                    Layout(layout.name,layout.sha)
                }!!

               Log.d(TAG, "getAllLayoutFiles: \n ${result.body()}")


            }else{
                Log.d(TAG, "getAllLayoutFiles: Null")
            }
        }
    }

    private fun getXml(username: String, repo: String,sha: String){

        CoroutineScope(Dispatchers.IO).launch {

        val result =  apiSet.getXml(username,repo,sha)

        if (result.isSuccessful){
            val xmlData = result.body()
            if (xmlData != null) {
              val  xmlContent = xmlData.content


                val decodedBytes = Base64.decode(xmlContent, Base64.DEFAULT)

                val decodedString = String(decodedBytes, StandardCharsets.UTF_8)

//                Log.d(TAG, "Decoded string : ${decodedString}")

                withContext(Dispatchers.Main) {
                    // Update UI elements here
                    code.text = decodedString

                }

            }

        }else{
            Log.d(TAG, "getXml: Null")
        }


    }
    }

    fun prevQuote(view: View) {
        if (count > 0) {
            count--
            val sha = layouts[count].sha
            val name = layouts[count].name
            Log.d(TAG, "prevQuote: $sha")


            val username = usernameEt.text.toString()
            val repo = repoEt.text.toString()

            getXml(username,repo,sha)
            updateUiWithName(name)
        } else {
            Log.d(TAG, "No previous quote available")
        }
    }

    private fun updateUiWithName(name: String) {
        xmlName.text = name

    }


    fun nextQuote(view: View) {
        if (count < layouts.size - 1) {
            count++
            val sha = layouts[count].sha
            val name = layouts[count].name
            Log.d(TAG, "nextQuote: $sha")

            val username = usernameEt.text.toString()
            val repo = repoEt.text.toString()

            getXml(username,repo,sha)
            updateUiWithName(name)
        } else {
            Log.d(TAG, "No next quote available")
        }
    }


    companion object{
        const val TAG = "TAG2"

    }

}
