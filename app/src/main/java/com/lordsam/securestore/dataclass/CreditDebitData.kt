package com.lordsam.securestore.dataclass

class CreditDebitData (
    val holderName: String,
    val accountNumber: Long,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvv: Int,
    val cardType: String
)