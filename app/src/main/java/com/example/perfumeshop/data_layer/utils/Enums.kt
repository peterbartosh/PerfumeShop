package com.example.perfumeshop.data_layer.utils

import com.google.common.collect.ImmutableList
import okhttp3.internal.immutableListOf


//@Stable
enum class ProductType{
    volume, tester, probe, licensed, auto, original, diffuser, lux;

    fun toRus() : String = when (this){
        volume -> "Объемы"
            tester -> "Тестеры"
            probe -> "Пробники"
            licensed -> "Лицензионные"
            auto -> "Авто"
            original -> "Оригиналы"
            diffuser -> "Диффузоры"
            lux -> "Люкс"
    }
}

fun getVolumes(type : ProductType) : List<String> =  when (type.name){
        ProductType.volume.name -> listOf("10", "15", "35", "45", "3x20", "80")
        ProductType.tester.name -> listOf("60", "65", "110", "115", "125")
        ProductType.probe.name -> listOf("30", "35", "55")
        else -> emptyList()
    }

//
//val productTypeEntities = immutableListOf(ProductType.volume, ProductType.tester, ProductType.probe,
//                                          ProductType.licensed, ProductType.auto, ProductType.original,
//                                          ProductType.diffuser, ProductType.lux)

enum class QueryType{
    collection, type, brand, volume, price, sex, is_on_hand;
}

enum class Sex {
    Male, Female, Unisex;
}

enum class OptionType{
    Nothing, Edit, Auth, Favourite, Orders,
    //About, Theme,
    PhoneNumber, WhatsApp, Telegram, Gmail;
}

val sexEntries = immutableListOf(Sex.Male, Sex.Female, Sex.Unisex)
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