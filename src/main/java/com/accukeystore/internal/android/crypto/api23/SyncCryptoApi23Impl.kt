package com.accukeystore.internal.android.crypto.api23

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.accukeystore.internal.android.crypto.SyncCrypto
import com.accukeystore.internal.android.crypto.api18.SyncCryptoApi18Impl
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.KeyPairGenerator
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.util.Calendar
import javax.security.auth.x500.X500Principal

/**
 * Implements [SyncCrypto] methods for API 23 (after the Android KeyStore public API), using
 * the introduced [KeyGenParameterSpec].
 */
class SyncCryptoApi23Impl @Throws(KeyStoreException::class)
constructor(context: Context) : SyncCryptoApi18Impl(context) {

  @TargetApi(Build.VERSION_CODES.M)
  @Throws(KeyStoreException::class)
  override fun create_key_if_not_available(alias: String) {
    if (!keyStore.containsAlias(alias)) {
      try {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 10)

        val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,
            SyncCryptoApi18Impl.ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setCertificateSubject(X500Principal(SyncCryptoApi18Impl.X500_PRINCIPAL))
            .setCertificateSerialNumber(BigInteger.ONE)
            .setCertificateNotBefore(start.time)
            .setCertificateNotAfter(end.time)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .setRandomizedEncryptionRequired(false)
            .build()
        keyGenerator.initialize(spec)
        keyGenerator.generateKeyPair()
      } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        throw KeyStoreException(e)
      } catch (e: InvalidAlgorithmParameterException) {
        e.printStackTrace()
        throw KeyStoreException(e)
      } catch (e: NoSuchProviderException) {
        e.printStackTrace()
        throw KeyStoreException(e)
      }

    }
  }
}
