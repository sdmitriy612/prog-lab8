package ru.sdmitriy612.gui.localization;

import java.util.ListResourceBundle;

public class Messages_es_NI extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"appTitle", "Aplicación de gestión de apartamentos"},
                {"userLabel", "Usuario:"},
                {"exitButton", "Salir"},
                {"filtersLabel", "Filtros"},
                {"idPrompt", "ID"},
                {"namePrompt", "Nombre"},
                {"xPrompt", "X"},
                {"yPrompt", "Y"},
                {"creationDatePrompt", "Fecha de creación"},
                {"areaPrompt", "Área"},
                {"roomsPrompt", "Habitaciones"},
                {"furnishPrompt", "Mobiliario"},
                {"viewPrompt", "Vista"},
                {"transportPrompt", "Transporte"},
                {"houseNamePrompt", "Nombre de la casa"},
                {"houseYearPrompt", "Año de la casa"},
                {"houseLiftsPrompt", "N. ascensores casa"},
                {"ownerIdPrompt", "ID propietario"},
                {"actionsLabel", "Acciones"},
                {"removeByIdLabel", "Eliminar por ID:"},
                {"executeButton", "Ejecutar"},
                {"removeAnyByViewLabel", "Eliminar cualquiera por vista:"},
                {"countGreaterThanFurnishLabel", "Contar mayor que mobiliario:"},
                {"executeScriptLabel", "Ejecutar script:"},
                {"pathPrompt", "Ruta del archivo de script"},
                {"chooseFileButton", "Seleccionar archivo"},
                {"infoButton", "Info"},
                {"clearButton", "Limpiar"},
                {"saveButton", "Guardar"},
                {"headButton", "Cabeza"},
                {"addButton", "Añadir"},
                {"addLabel", "Añadir"},
                {"updateButton", "Actualizar"},
                {"showButton", "Mostrar"},
                {"minByIdButton", "Min por ID"},
                {"filterButton", "Filtrar"},

                // Table Columns
                {"columnId", "ID"},
                {"columnName", "Nombre"},
                {"columnX", "X"},
                {"columnY", "Y"},
                {"columnCreationDate", "Fecha de creación"},
                {"columnArea", "Área"},
                {"columnRooms", "Habitaciones"},
                {"columnFurnish", "Mobiliario"},
                {"columnView", "Vista"},
                {"columnTransport", "Transporte"},
                {"columnHouseName", "Nombre de la casa"},
                {"columnHouseYear", "Año de la casa"},
                {"columnLifts", "N. ascensores casa"},
                {"columnUserOwnerId", "ID propietario"}, // Changed from colUserOwnerId
                {"columnActions", "Acciones"}, // Changed from colActions

                // Alert messages
                {"invalidId", "ID inválido. Por favor, introduzca un número."},
                {"selectViewPrompt", "Por favor, seleccione una vista para eliminar."},
                {"selectFurnishPrompt", "Por favor, seleccione un tipo de mobiliario para contar."},
                {"scriptPathEmpty", "La ruta del script no puede estar vacía."},
                {"errorLoadingFlatForm", "Error al cargar el formulario del apartamento: "},
                {"errorLoadingFlatUpdateForm", "Error al cargar el formulario de actualización del apartamento: "},
                {"errorLoadingFlatInfoWindow", "Error al cargar la ventana de información del apartamento: "},
                {"server", "Servidor"},
                {"jsonParsingError", "Error al analizar JSON para el comando '"},
                {"serverErrorTitle", "Error del servidor"},
                {"serverResponseTitle", "Respuesta del servidor"},

                // File Chooser
                {"chooseScriptFileTitle", "Seleccionar archivo de script"},
                {"textFiles", "Archivos de texto"},
                {"allFiles", "Todos los archivos"},

                // Stage Titles
                {"addFlatTitle", "Añadir apartamento"},
                {"updateFlatTitle", "Actualizar apartamento"},
                {"flatInformationTitle", "Información apartamento"},

                // Locale selection
                {"language", "Idioma:"},
                {"ru_RU", "Ruso"},
                {"fi_FI", "Finlandés"},
                {"it_IT", "Italiano"},
                {"es_NI", "Español (Nicaragua)"},

                // Добавлены для flat_info_dialog.fxml
                {"flatInfoTitle", "Información apartamento"},
                {"coordinates:", "Coordenadas:"},
                {"house:", "Casa:"},
                {"editButton", "Editar"},
                {"closeButton", "Cerrar"},
                {"columnId:", "ID:"},
                {"columnName:", "Nombre:"},
                {"columnCreationDate:", "Fecha de creación:"},
                {"columnArea:", "Área:"},
                {"columnRooms:", "Habitaciones:"},
                {"columnFurnish:", "Mobiliario:"},
                {"columnView:", "Vista:"},
                {"columnTransport:", "Transporte:"},
                {"columnUserOwnerId:", "ID propietario:"},
                {"columnHouse:", "Casa:"},

                // Новые ключи из main_window.fxml
                {"enterFlatButton", "Introducir datos del apartamento"},
                {"addIfMaxLabel", "Añadir si es máximo"},
                {"removeLowerLabel", "Eliminar inferiores"},
                {"helpLabel", "Ayuda"},
                {"clearLabel", "Limpiar"},
                {"countByFurnishLabel", "Contar por mobiliario"},
                {"historyLabel", "Historial"},
                {"infoLabel", "Información"},

                {"formTitle", "Introducir apartamento"},
                {"formNameLabel", "Nombre:"},
                {"formNamePrompt", "Ingrese el nombre del apartamento"},
                {"formXLabel", "X (Coordenadas):"},
                {"formXPrompt", "Número (máx 497)"},
                {"formYLabel", "Y (Coordenadas):"},
                {"formYPrompt", "Número (> -738)"},
                {"formAreaLabel", "Área:"},
                {"formAreaPrompt", "Área (> 0)"},
                {"formRoomsLabel", "Número de habitaciones:"},
                {"formRoomsPrompt", "No. de habitaciones (1-20)"},
                {"formFurnishLabel", "Mobiliario:"},
                {"formViewLabel", "Vista:"},
                {"formTransportLabel", "Transporte:"},
                {"formHouseNameLabel", "Nombre de la casa:"},
                {"formHouseNamePrompt", "Nombre de la casa"},
                {"formHouseYearLabel", "Año de la casa:"},
                {"formHouseYearPrompt", "Año (> 0)"},
                {"formLiftsLabel", "Número de ascensores:"},
                {"formLiftsPrompt", "No. de ascensores (> 0)"},
                {"formSaveButton", "Guardar"},
                {"formCancelButton", "Cancelar"},

                {"authTitle", "Iniciar Sesión"},
                {"authUsernamePrompt", "Nombre de usuario"},
                {"authPasswordPrompt", "Contraseña"},
                {"authLoginButton", "Iniciar Sesión"},
                {"authRegisterButton", "Registrarse"},

                {"authStageTitle", "Flat Collector - Autorización"},
                {"mainStageTitle", "Flat Collector"},
        };
    }
}