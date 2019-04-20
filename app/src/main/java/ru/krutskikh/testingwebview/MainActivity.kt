package ru.krutskikh.testingwebview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_main.*
import android.webkit.WebSettings
import android.support.v4.view.ViewCompat.setLayerType
import android.view.View
import android.webkit.JavascriptInterface
import android.content.Context
import android.util.Log
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private final val rowCount = 200
    private final val colCount = 200
    private final val VW_BEGIN = """
        <!DOCTYPE html>
        <html>
            <head>
                <meta charset="utf-8" />
                <title>Тестирование WebView</title>
            </head>
            <body>
                <table border="1">
    """
    private final val VW_END = """
                <script>
                    function showMessage(row, col){
                        event.target.style.backgroundColor = getRandomColor();
                        JSInterface.showMessage(row, col)
                    }
                    function getRandomColor() {
                        var letters = '0123456789ABCDEF';
                        var color = '#';
                        for (var i = 0; i < 6; i++) {
                            color += letters[Math.floor(Math.random() * 16)];
                        }
                        return color;
                    }

                </script>
            </body>
        </html>
    """


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        WebView.setWebContentsDebuggingEnabled(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.clearCache(true);
        web_view.setInitialScale(1);
        web_view.getSettings().setLoadWithOverviewMode(true)
        web_view.getSettings().setUseWideViewPort(true)
        web_view.getSettings().setBuiltInZoomControls(true)
        web_view.getSettings().setDisplayZoomControls(false)
        web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        val jsInterface: JSInterface = JSInterface(applicationContext);
        web_view.addJavascriptInterface(jsInterface, "JSInterface");
        web_view.loadData(getHTML(), "text/html", null)
    }

    fun getHTML(): String {
        var result = VW_BEGIN
        result += createTable()
        result += VW_END
        return result
    }

    fun createTable(): String {
        var tbl: StringBuilder = StringBuilder("")
        tbl.append("<tr>")
        // header cols num
        for (col in 0..colCount) {
            tbl.append("<th>$col</th>")
        }
        tbl.append("</tr>")

        // cells
        for (row in 0..rowCount) {
            tbl.append("<tr>")
            for (col in 0..colCount) {
                tbl.append("<td onclick=\"showMessage($row, $col)\">$row - $col</td>")
            }
            tbl.append("</tr>")
        }
        return tbl.toString()
    }

    class JSInterface(c: Context) {
        var mContext: Context = c

        @JavascriptInterface
        fun showMessage(row: Int, col: Int) {
            Log.e("WEBVIEW_showMessage", "showMessage")
            Toast.makeText(mContext, "Clicked cell $row:$col", Toast.LENGTH_SHORT).show()
        }
    }
}
