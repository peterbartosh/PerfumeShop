package com.example.perfumeshop.data_layer.utils



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

val sexEntities = listOf(Sex.Male, Sex.Female, Sex.Unisex)

val queryTypeEntities = listOf(QueryType.collection, QueryType.type, QueryType.brand,
                               QueryType.volume, QueryType.price, QueryType.sex, QueryType.is_on_hand)

val optionTypeEntities = listOf(OptionType.Nothing, OptionType.Edit,
                                OptionType.Favourite, OptionType.Orders,
                                //OptionType.About, OptionType.Theme,
                                //OptionType.PhoneNumber, OptionType.WhatsApp,
                                //OptionType.Telegram, OptionType.Gmail)
)