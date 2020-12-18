package com.kbds.kbidpassreader.extension

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val AES_KEY = "MOBILEKBMOBILEKBMOBILEKBMOBILEKB"
const val AES_IV = "MOBILEKBMOBILEKB"

private fun getSecretKey() : SecretKey {
    try {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(AES_KEY.toByteArray())

        return SecretKeySpec(AES_KEY.toByteArray(), "AES")

    } catch (e: Exception) {
        throw e
    }
}

fun String.encryptAes256() : String {
    try {
        val ivParameterSpec = IvParameterSpec(AES_IV.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), ivParameterSpec)

        return Base64.encodeToString(cipher.doFinal(this.toByteArray()), Base64.NO_PADDING)

    } catch (e: Exception) {
        throw e
    }
}

fun String.decryptAes256() : String {
    try {
        val ivParameterSpec = IvParameterSpec(AES_IV.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), ivParameterSpec)

        return String(cipher.doFinal(Base64.decode(this, Base64.NO_PADDING)))

    } catch (e: Exception) {
        throw e
    }
}

fun String.digestSha256() : String {
    try {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(this.toByteArray())

        return messageDigest.digest().encodeHex()

    } catch (e: Exception) {
        throw e
    }
}

fun String.encodeBase64() : String {
    try {
        return Base64.encodeToString(this.toByteArray(), Base64.NO_PADDING)

    } catch (e: Exception) {
        throw e
    }
}

fun String.decodeBase64() : String {
    try {
        return String(Base64.decode(this, Base64.NO_PADDING))

    } catch (e: Exception) {
        throw e
    }
}

fun ByteArray.encodeHex() : String {
    try {
        return joinToString("") { "%02x".format(it) }

    } catch (e: Exception) {
        throw e
    }
}