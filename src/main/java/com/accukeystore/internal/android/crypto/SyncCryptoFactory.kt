/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.accukeystore.internal.android.crypto

import android.content.Context
import android.os.Build
import com.accukeystore.internal.android.crypto.api18.SyncCryptoApi18Impl
import com.accukeystore.internal.android.crypto.api23.SyncCryptoApi23Impl
import com.accukeystore.internal.android.crypto.apilegacy.SyncCryptoLegacy
import java.security.KeyStoreException

/**
 */
object SyncCryptoFactory {

  private val IS_API_23 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
  private val IS_API_18 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
  private val IS_API_LEGACY = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD

  @Throws(KeyStoreException::class)
  fun get(context: Context): SyncCrypto {
    return if (IS_API_23) {
      SyncCryptoApi23Impl(context)
    } else if (IS_API_18) {
      SyncCryptoApi18Impl(context)
    } else if (IS_API_LEGACY) {
      SyncCryptoLegacy(context)
    } else {
      throw KeyStoreException("Unknown android version")
    }
  }
}
