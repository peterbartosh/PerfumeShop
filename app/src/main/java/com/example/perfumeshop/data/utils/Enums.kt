package com.example.perfumeshop.data.utils

import okhttp3.internal.immutableListOf

enum class UserPreferencesType{
    Theme, FontSize;
}
enum class OrderStatus{
    Processing, Accepted, Delivering, Succeed, Canceled;

    fun toRus() : String{
        return when (this){
            Processing -> "В обработке"
            Accepted -> "Принят"
            Delivering -> "В пути"
            Succeed -> "Успешно"
            Canceled -> "Отменён"
        }
    }

}

enum class ProductType{
    volume, tester, probe, licensed, auto, original, diffuser, lux, notSpecified;

    fun toRus() : String = when (this){
            volume -> "Объемы"
            tester -> "Тестеры"
            probe -> "Пробники"
            licensed -> "Лицензионные"
            auto -> "Авто"
            original -> "Оригиналы"
            diffuser -> "Диффузоры"
            lux -> "Люкс"
            notSpecified -> "Не задано"
    }
}

fun getVolumes(type : ProductType) : List<String> =  when (type.name){
        ProductType.volume.name -> listOf("10", "15", "35", "45", "3x20", "80")
        ProductType.tester.name -> listOf("60", "65", "110", "115", "125")
        ProductType.probe.name -> listOf("30", "35", "55")
        else -> listOf("50")
    }

//
//val productTypeEntities = immutableListOf(ProductType.volume, ProductType.tester, ProductType.probe,
//                                          ProductType.licensed, ProductType.auto, ProductType.original,
//                                          ProductType.diffuser, ProductType.lux)

enum class QueryType{
    type, brand, volume, price, sex, is_on_hand;
}

enum class ProductSex {
    Male, Female, Unisex;

}

enum class UserSex{
    Male, Female, NotSpecified;
}

fun getSexByName(name : String?) : UserSex {
    return when(name){
        "Male" -> UserSex.Male
        "Female" -> UserSex.Female
        else -> UserSex.NotSpecified
    }
}

enum class OptionType{
    Nothing, Edit, Auth, Favourite, Orders,
    //About, Theme,
    PhoneNumber, WhatsApp, Telegram, Gmail;
}

val productSexEntries = immutableListOf(ProductSex.Male, ProductSex.Female, ProductSex.Unisex)
//
//val queryTypeEntries = immutableListOf(QueryType.collection, QueryType.type, QueryType.brand,
//                               QueryType.volume, QueryType.price, QueryType.sex, QueryType.is_on_hand)
//
//val optionTypeEntries = immutableListOf(OptionType.Nothing, OptionType.Edit,
//                                OptionType.Favourite, OptionType.Orders,
//                                //OptionType.About, OptionType.Theme,
//                                //OptionType.PhoneNumber, OptionType.WhatsApp,
//                                //OptionType.Telegram, OptionType.Gmail)
//)