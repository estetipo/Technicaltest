package com.carlos.satori.technical_test.api

class Token {
    companion object{
        @JvmStatic
        var token:String = "b990b089cf341065d3bf65aebd6520f1"
            set(value) {
                field = value
                retrofitInstance = null
            }

        @JvmStatic
        var retrofitInstance: ApiService? = null
    }
}