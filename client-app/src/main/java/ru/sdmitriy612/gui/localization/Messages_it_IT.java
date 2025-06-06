package ru.sdmitriy612.gui.localization;

import java.util.ListResourceBundle;

public class Messages_it_IT extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"appTitle", "Applicazione di gestione appartamenti"},
                {"userLabel", "Utente:"},
                {"exitButton", "Esci"},
                {"filtersLabel", "Filtri"},
                {"idPrompt", "ID"},
                {"namePrompt", "Nome"},
                {"xPrompt", "X"},
                {"yPrompt", "Y"},
                {"creationDatePrompt", "Data di creazione"},
                {"areaPrompt", "Area"},
                {"roomsPrompt", "Stanze"},
                {"furnishPrompt", "Arredamento"},
                {"viewPrompt", "Vista"},
                {"transportPrompt", "Trasporto"},
                {"houseNamePrompt", "Nome casa"},
                {"houseYearPrompt", "Anno costruzione casa"},
                {"houseLiftsPrompt", "N. ascensori casa"},
                {"ownerIdPrompt", "ID proprietario"},
                {"actionsLabel", "Azioni"},
                {"removeByIdLabel", "Rimuovi per ID:"},
                {"executeButton", "Esegui"},
                {"removeAnyByViewLabel", "Rimuovi qualsiasi per vista:"},
                {"countGreaterThanFurnishLabel", "Conta maggiore di arredamento:"},
                {"executeScriptLabel", "Esegui script:"},
                {"pathPrompt", "Percorso file script"},
                {"chooseFileButton", "Scegli file"},
                {"infoButton", "Info"},
                {"clearButton", "Cancella"},
                {"saveButton", "Salva"},
                {"headButton", "Testa"},
                {"addButton", "Aggiungi"},
                {"addLabel", "Aggiungi"},
                {"updateButton", "Aggiorna"},
                {"showButton", "Mostra"},
                {"minByIdButton", "Min per ID"},
                {"filterButton", "Filtra"},

                // Table Columns
                {"columnId", "ID"},
                {"columnName", "Nome"},
                {"columnX", "X"},
                {"columnY", "Y"},
                {"columnCreationDate", "Data di creazione"},
                {"columnArea", "Area"},
                {"columnRooms", "Stanze"},
                {"columnFurnish", "Arredamento"},
                {"columnView", "Vista"},
                {"columnTransport", "Trasporto"},
                {"columnHouseName", "Nome casa"},
                {"columnHouseYear", "Anno costruzione casa"},
                {"columnLifts", "N. ascensori casa"},
                {"columnUserOwnerId", "ID proprietario"}, // Changed from ownerIdPrompt
                {"columnActions", "Azioni"}, // Changed from colActions

                // Alert messages
                {"invalidId", "ID non valido. Inserire un numero."},
                {"selectViewPrompt", "Selezionare una vista da rimuovere."},
                {"selectFurnishPrompt", "Selezionare un tipo di arredamento da contare."},
                {"scriptPathEmpty", "Il percorso dello script non può essere vuoto."},
                {"errorLoadingFlatForm", "Errore nel caricamento del modulo appartamento: "},
                {"errorLoadingFlatUpdateForm", "Errore nel caricamento del modulo di aggiornamento appartamento: "},
                {"errorLoadingFlatInfoWindow", "Errore nel caricamento della finestra informazioni appartamento: "},
                {"server", "Server"},
                {"jsonParsingError", "Errore di parsing JSON per il comando '"},
                {"serverErrorTitle", "Errore del server"},
                {"serverResponseTitle", "Risposta del server"},

                // File Chooser
                {"chooseScriptFileTitle", "Seleziona file script"},
                {"textFiles", "File di testo"},
                {"allFiles", "Tutti i file"},

                // Stage Titles
                {"addFlatTitle", "Aggiungi appartamento"},
                {"updateFlatTitle", "Aggiorna appartamento"},
                {"flatInformationTitle", "Informazioni appartamento"},

                // Locale selection
                {"language", "Lingua:"},
                {"ru_RU", "Russo"},
                {"fi_FI", "Finlandese"},
                {"it_IT", "Italiano"},
                {"es_NI", "Spagnolo (Nicaragua)"},

                // Добавлены для flat_info_dialog.fxml
                {"flatInfoTitle", "Informazioni appartamento"},
                {"coordinates:", "Coordinate:"},
                {"house:", "Casa:"},
                {"editButton", "Modifica"},
                {"closeButton", "Chiudi"},
                {"columnId:", "ID:"},
                {"columnName:", "Nome:"},
                {"columnCreationDate:", "Data di creazione:"},
                {"columnArea:", "Area:"},
                {"columnRooms:", "Stanze:"},
                {"columnFurnish:", "Arredamento:"},
                {"columnView:", "Vista:"},
                {"columnTransport:", "Trasporto:"},
                {"columnUserOwnerId:", "ID proprietario:"},
                {"columnHouse:", "Casa:"},

                // Новые ключи из main_window.fxml
                {"enterFlatButton", "Inserisci dati appartamento"},
                {"addIfMaxLabel", "Aggiungi se massimo"},
                {"removeLowerLabel", "Rimuovi inferiori"},
                {"helpLabel", "Aiuto"},
                {"clearLabel", "Pulisci"},
                {"countByFurnishLabel", "Conta per arredamento"},
                {"historyLabel", "Cronologia"},
                {"infoLabel", "Informazioni"},

                {"formTitle", "Inserisci appartamento"},
                {"formNameLabel", "Nome:"},
                {"formNamePrompt", "Inserisci il nome dell'appartamento"},
                {"formXLabel", "X (Coordinate):"},
                {"formXPrompt", "Numero (max 497)"},
                {"formYLabel", "Y (Coordinate):"},
                {"formYPrompt", "Numero (> -738)"},
                {"formAreaLabel", "Area:"},
                {"formAreaPrompt", "Area (> 0)"},
                {"formRoomsLabel", "Numero di stanze:"},
                {"formRoomsPrompt", "Numero di stanze (1-20)"},
                {"formFurnishLabel", "Arredamento:"},
                {"formViewLabel", "Vista:"},
                {"formTransportLabel", "Trasporto:"},
                {"formHouseNameLabel", "Nome casa:"},
                {"formHouseNamePrompt", "Nome casa"},
                {"formHouseYearLabel", "Anno di costruzione casa:"},
                {"formHouseYearPrompt", "Anno (> 0)"},
                {"formLiftsLabel", "Numero di ascensori:"},
                {"formLiftsPrompt", "Numero di ascensori (> 0)"},
                {"formSaveButton", "Salva"},
                {"formCancelButton", "Annulla"},

                {"authTitle", "Accesso al sistema"},
                {"authUsernamePrompt", "Nome utente"},
                {"authPasswordPrompt", "Password"},
                {"authLoginButton", "Accedi"},
                {"authRegisterButton", "Registrati"},

                {"authStageTitle", "Flat Collector - Autorizzazione"},
                {"mainStageTitle", "Flat Collector"},
        };
    }
}