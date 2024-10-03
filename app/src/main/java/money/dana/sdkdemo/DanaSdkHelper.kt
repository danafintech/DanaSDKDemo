package money.dana.sdkdemo

import android.app.Activity
import com.danafintech.danasdk.config.DanaConfig
import com.danafintech.danasdk.config.DanaEnvironment
import com.danafintech.danasdk.config.DanaSdk
import com.danafintech.danasdk.config.User

import com.danafintech.danacreditscorehelper.state.DanaCreditScoreConfig
import com.danafintech.danacreditscorehelper.utils.DanaSMSEnvironment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LifecycleOwner

class DanaSdkHelper {
    companion object {
        @JvmStatic
        fun launchDanaSdk(phone:String, activity: Activity) {
            val config = DanaConfig(
                companyKey = "", // TODO: Replace
                user = User(
                    phone = phone,
                    firebaseToken = ""
                ),
                userData = mapOf(),
                environment = DanaEnvironment.DEVELOPMENT,
                showActionBar = false,
                showDanaDialog = false,
                onLogOut = {} // TODO: Implement your logout logic
            )
            DanaSdk(config).launchSdk(activity)
        }

        /**
         * This method is used to run Dana SMS SDK Separately.
         * @param activity The activity of the application.
         *
         * If you don't need to run this separately, you can remove this method.
         */
        @JvmStatic
        fun runSmsSdk(activity: Activity) {
            (activity as? LifecycleOwner)?.lifecycleScope?.launch {
                val isSend =
                    DanaCreditScoreConfig(
                        "",
                        "",
                        DanaSMSEnvironment.DEVELOPMENT,
                    ).initializeSmsParsing(activity)
                if (isSend) {
                    Log.d("CreditScoreHelper", "Successfully submitted transaction data")
                } else {
                    Log.d("CreditScoreHelper", "Failed to submit transaction data")
                }
            }
        }
    }
}