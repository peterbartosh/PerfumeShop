package com.example.perfumeshop.data.utils

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

    Original, Tester, Probe, Auto, Diffuser, Compact, Licensed, Lux, Selectives, EuroA, NotSpecified;

    companion object {
        private val types =
            listOf(Original, Tester, Probe, Auto, Diffuser, Compact, Licensed, Lux, Selectives, EuroA, NotSpecified)

        fun getType(ind : Int) = if (ind in types.indices) types[ind] else NotSpecified

        fun getTypes() : List<ProductType> = types

    }

    fun toRus() : String = when (this) {
        Original -> "Оригиналы"
        Tester -> "Тестеры"
        Probe -> "Пробники"
        Auto -> "Авто"
        Diffuser -> "Диффузоры"
        Compact -> "Компакт"
        Licensed -> "Лицензионные"
        Lux -> "Люкс"
        Selectives -> "Селективы"
        EuroA -> "Евро А+"
        NotSpecified -> "Не задано"
    }

    fun getVolumes(): List<Double> = when (this) {
        Tester -> listOf(55.0, 60.0, 65.0, 110.0, 115.0, 125.0)
        Probe -> listOf(30.0, 35.0)
        Compact -> listOf(15.0, 45.0, 35.0, 80.0, 10.0, 60.0, 100.0)
        else -> emptyList()
    }
}


enum class QueryType{
    Type, Brand, TypeVolume;
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
    PhoneNumber, Gmail, Telegram, WhatsApp, WebSite;
}
