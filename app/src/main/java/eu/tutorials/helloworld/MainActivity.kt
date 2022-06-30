package eu.tutorials.helloworld

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var context : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("Loaded")
        context = this

        //val button = findViewById<Button>(R.id.button)
        var increment = 0
        button.setOnClickListener {
            increment++
            textView.text = increment.toString()
        }
        getContent().execute()

    }
    internal inner class getContent : AsyncTask<Void, Void, String>() {

        lateinit var progessDialog: ProgressDialog
        var hasInternet = false

        override fun onPreExecute() {
            super.onPreExecute()
            progessDialog = ProgressDialog(context)
            progessDialog.setMessage("Downloading")
            progessDialog.setCancelable(false)
            progessDialog.show()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progessDialog.dismiss()
            if(hasInternet) {
                tv_result.text = result
            } else {
                tv_result.text = "No Internet"
            }
        }

        override fun doInBackground(vararg p0: Void?): String {
            var resp = ""
            if(isNetWorkAvailable()) {
                hasInternet = true
                return getJSONObjectFromURL("https://henning-weise-todo.herokuapp.com/todo/")
            }
            return resp
        }

        private fun isNetWorkAvailable(): Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        @Throws(IOException::class, JSONException::class)
        fun getJSONObjectFromURL(urlString: String): String {
            var urlConnection: HttpURLConnection? = null
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.doOutput = true
            urlConnection.connect()
            val br = BufferedReader(InputStreamReader(url.openStream()))
            val sb = StringBuilder()
            var line: String
            while (br.readLine().also { line = it } != null) {
                sb.append(
                    """
                $line
                
                """.trimIndent()
                )
            }
            br.close()
            val jsonString = sb.toString()
            println("JSON: $jsonString")
            return jsonString
        }

    }
}