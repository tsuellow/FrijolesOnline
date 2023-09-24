package com.ccy.apps.frijolesonline.common

object Constants {
    val URL_FIREBASE="https://frijolesonline-default-rtdb.firebaseio.com/"
    val URL_STORAGE=""

    val MUNICIPIOS= listOf<String>(
        "Atlántico Norte - Bonanza",
        "Atlántico Norte - Mulukuku",
        "Atlántico Norte - Prinzapolka",
        "Atlántico Norte - Puerto Cabezas",
        "Atlántico Norte - Rosita",
        "Atlántico Norte - Siuna",
        "Atlántico Norte - Waslala",
        "Atlántico Norte - Waspán",
        "Atlántico Sur - Bluefields",
        "Atlántico Sur - Corn Island",
        "Atlántico Sur - Desembocadura de Río Grande",
        "Atlántico Sur - El Ayote",
        "Atlántico Sur - El Tortuguero",
        "Atlántico Sur - Kukra - Hill",
        "Atlántico Sur - La Cruz de Río Grande",
        "Atlántico Sur - Laguna de Perlas",
        "Atlántico Sur - Muelle de los Bueyes",
        "Atlántico Sur - Nueva Guinea",
        "Atlántico Sur - Paiwas",
        "Atlántico Sur - Rama",
        "Boaco - Boaco",
        "Boaco - Camoapa",
        "Boaco - San  Lorenzo",
        "Boaco - San José de los Remates",
        "Boaco - Santa Lucía",
        "Boaco - Teustepe",
        "Carazo - Diriamba",
        "Carazo - Dolores",
        "Carazo - El Rosario",
        "Carazo - Jinotepe",
        "Carazo - La Conquista",
        "Carazo - La Paz de Carazo",
        "Carazo - San Marcos",
        "Carazo - Santa Teresa",
        "Chinandega - Chichigalpa",
        "Chinandega - Chinandega",
        "Chinandega - Cinco Pinos",
        "Chinandega - Corinto",
        "Chinandega - El Realejo",
        "Chinandega - El Viejo",
        "Chinandega - Posoltega",
        "Chinandega - Puerto Morazán",
        "Chinandega - San Francisco del Norte",
        "Chinandega - San Pedro del Norte",
        "Chinandega - Santo Tomás del Norte",
        "Chinandega - Somotillo",
        "Chinandega - Villanueva",
        "Chontales - Acoyapa",
        "Chontales - Comalapa",
        "Chontales - El Coral",
        "Chontales - Juigalpa",
        "Chontales - La Libertad",
        "Chontales - San Francisco Cuapa",
        "Chontales - San Pedro de Lóvago",
        "Chontales - Santo Domingo",
        "Chontales - Santo Tomás",
        "Chontales - Villa Sandino",
        "Estelí - Condega",
        "Estelí - Estelí",
        "Estelí - La Trinidad",
        "Estelí - Pueblo Nuevo",
        "Estelí - San Juan de Limay",
        "Estelí - San Nicolás",
        "Granada - Diriá",
        "Granada - Diriomo",
        "Granada - Granada",
        "Granada - Nandaime",
        "Jinotega - El Cúa",
        "Jinotega - Jinotega",
        "Jinotega - La Concordia",
        "Jinotega - San José Bocay",
        "Jinotega - San Rafael del Norte",
        "Jinotega - San Sebastián de Yalí",
        "Jinotega - Sta. María de Pantasma",
        "Jinotega - Wiwilí",
        "León - Achuapa",
        "León - El Jicaral",
        "León - El Sauce",
        "León - La Paz Centro",
        "León - Larreynaga",
        "León - León",
        "León - Nagarote",
        "León - Quezalguaque",
        "León - Santa Rosa del Peñón",
        "León - Telica",
        "Madriz - Las Sabanas",
        "Madriz - Palacagüina",
        "Madriz - San José de Cusmapa",
        "Madriz - San Juan de Río Coco",
        "Madriz - San Lucas",
        "Madriz - Somoto",
        "Madriz - Telpaneca",
        "Madriz - Totogalpa",
        "Madriz - Yalagüina",
        "Managua - Ciudad Sandino",
        "Managua - El Crucero",
        "Managua - Managua",
        "Managua - Distrito II",
        "Managua - Distrito III",
        "Managua - Distrito IV",
        "Managua - Distrito V",
        "Managua - Distrito VI",
        "Managua - Mateare",
        "Managua - San Francisco Libre",
        "Managua - San Rafael del Sur",
        "Managua - Ticuantepe",
        "Managua - Tipitapa",
        "Managua - Villa Carlos Fonseca",
        "Masaya - Catarina",
        "Masaya - La Concepción",
        "Masaya - Masatepe",
        "Masaya - Masaya",
        "Masaya - Nandasmo",
        "Masaya - Nindirí",
        "Masaya - Niquinohomo",
        "Masaya - San Juan de Oriente",
        "Masaya - Tisma",
        "Matagalpa - Ciudad Darío",
        "Matagalpa - El Tuma - La Dalia",
        "Matagalpa - Esquipulas",
        "Matagalpa - Matagalpa",
        "Matagalpa - Matiguás",
        "Matagalpa - Muy Muy",
        "Matagalpa - Rancho Grande",
        "Matagalpa - Río Blanco",
        "Matagalpa - San Dionisio",
        "Matagalpa - San Isidro",
        "Matagalpa - San Ramón",
        "Matagalpa - Sébaco",
        "Matagalpa - Terrabona",
        "Nueva Segovia - Ciudad Antigua",
        "Nueva Segovia - Dipilto",
        "Nueva Segovia - El Jícaro",
        "Nueva Segovia - Jalapa",
        "Nueva Segovia - Macuelizo",
        "Nueva Segovia - Mozonte",
        "Nueva Segovia - Murra",
        "Nueva Segovia - Ocotal",
        "Nueva Segovia - Quilalí",
        "Nueva Segovia - San Fernando",
        "Nueva Segovia - Santa María",
        "Nueva Segovia - Wiwili de Nueva Segovia",
        "Río San Juan - El Almendro",
        "Río San Juan - El Castillo",
        "Río San Juan - Morrito",
        "Río San Juan - San Carlos",
        "Río San Juan - San Juan del Nicaragua",
        "Río San Juan - San Miguelito",
        "Rivas - Altagracia",
        "Rivas - Belén",
        "Rivas - Buenos Aires",
        "Rivas - Cárdenas",
        "Rivas - Moyogalpa",
        "Rivas - Potosí",
        "Rivas - Rivas",
        "Rivas - San Jorge",
        "Rivas - San Juan del Sur",
        "Rivas - Tola",
    )
}