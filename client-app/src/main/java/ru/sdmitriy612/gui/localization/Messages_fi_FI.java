package ru.sdmitriy612.gui.localization;

import java.util.ListResourceBundle;

public class Messages_fi_FI extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"appTitle", "Asuntojen hallintasovellus"},
                {"userLabel", "Käyttäjä:"},
                {"exitButton", "Poistu"},
                {"filtersLabel", "Suodattimet"},
                {"idPrompt", "ID"},
                {"namePrompt", "Nimi"},
                {"xPrompt", "X"},
                {"yPrompt", "Y"},
                {"creationDatePrompt", "Luontipäivä"},
                {"areaPrompt", "Pinta-ala"},
                {"roomsPrompt", "Huoneet"},
                {"furnishPrompt", "Kalustus"},
                {"viewPrompt", "Näkymä"},
                {"transportPrompt", "Kuljetus"},
                {"houseNamePrompt", "Talon nimi"},
                {"houseYearPrompt", "Talon rakennusvuosi"},
                {"houseLiftsPrompt", "Hissit talossa"},
                {"ownerIdPrompt", "Omistajan ID"},
                {"actionsLabel", "Toiminnot"},
                {"removeByIdLabel", "Poista ID:llä:"},
                {"executeButton", "Suorita"},
                {"removeAnyByViewLabel", "Poista mikä tahansa näkymän perusteella:"},
                {"countGreaterThanFurnishLabel", "Laske suurempi kuin kalustus:"},
                {"executeScriptLabel", "Suorita skripti:"},
                {"pathPrompt", "Skriptitiedoston polku"},
                {"chooseFileButton", "Valitse tiedosto"},
                {"infoButton", "Tiedot"},
                {"clearButton", "Tyhjennä"},
                {"saveButton", "Tallenna"},
                {"headButton", "Ensimmäinen"},
                {"addButton", "Lisää"},
                {"addLabel", "Lisää"},
                {"updateButton", "Päivitä"},
                {"showButton", "Näytä"},
                {"minByIdButton", "Min ID:llä"},
                {"filterButton", "Suodata"},

                // Table Columns
                {"columnId", "ID"},
                {"columnName", "Nimi"},
                {"columnX", "X"},
                {"columnY", "Y"},
                {"columnCreationDate", "Luontipäivä"},
                {"columnArea", "Pinta-ala"},
                {"columnRooms", "Huoneet"},
                {"columnFurnish", "Kalustus"},
                {"columnView", "Näkymä"},
                {"columnTransport", "Kuljetus"},
                {"columnHouseName", "Talon nimi"},
                {"columnHouseYear", "Talon vuosi"},
                {"columnLifts", "Hissit talossa"},
                {"columnUserOwnerId", "Omistajan ID"}, // Changed from colUserOwnerId
                {"columnActions", "Toiminnot"}, // Changed from colActions

                // Alert messages
                {"invalidId", "Virheellinen ID. Anna numero."},
                {"selectViewPrompt", "Valitse näkymä poistettavaksi."},
                {"selectFurnishPrompt", "Valitse kalustustyyppi laskettavaksi."},
                {"scriptPathEmpty", "Skriptipolku ei voi olla tyhjä."},
                {"errorLoadingFlatForm", "Virhe asuntolomakkeen lataamisessa: "},
                {"errorLoadingFlatUpdateForm", "Virhe asunnon päivityslomakkeen lataamisessa: "},
                {"errorLoadingFlatInfoWindow", "Virhe asuntotietoikkunan lataamisessa: "},
                {"server", "Palvelin"},
                {"jsonParsingError", "JSON-jäsennyksen virhe komennolle '"},
                {"serverErrorTitle", "Palvelinvirhe"},
                {"serverResponseTitle", "Palvelimen vastaus"},

                // File Chooser
                {"chooseScriptFileTitle", "Valitse skriptitiedosto"},
                {"textFiles", "Tekstitiedostot"},
                {"allFiles", "Kaikki tiedostot"},

                // Stage Titles
                {"addFlatTitle", "Lisää asunto"},
                {"updateFlatTitle", "Päivitä asunto"},
                {"flatInformationTitle", "Asunnon tiedot"},

                // Locale selection
                {"language", "Kieli:"},
                {"ru_RU", "Venäjä"},
                {"fi_FI", "Suomi"},
                {"it_IT", "Italia"},
                {"es_NI", "Espanja (Nicaragua)"},

                // Добавлены для flat_info_dialog.fxml
                {"flatInfoTitle", "Asunnon tiedot"},
                {"coordinates:", "Koordinaatit:"},
                {"house:", "Talo:"},
                {"editButton", "Muokkaa"},
                {"closeButton", "Sulje"},
                {"columnId:", "ID:"},
                {"columnName:", "Nimi:"},
                {"columnCreationDate:", "Luontipäivä:"},
                {"columnArea:", "Pinta-ala:"},
                {"columnRooms:", "Huoneet:"},
                {"columnFurnish:", "Kalustus:"},
                {"columnView:", "Näkymä:"},
                {"columnTransport:", "Kuljetus:"},
                {"columnUserOwnerId:", "Omistajan ID:"},
                {"columnHouse:", "Talo:"},

                // Новые ключи из main_window.fxml
                {"enterFlatButton", "Syötä asunnon tiedot"},
                {"addIfMaxLabel", "Lisää jos suurin"},
                {"removeLowerLabel", "Poista pienemmät"},
                {"helpLabel", "Ohje"},
                {"clearLabel", "Tyhjennä"},
                {"countByFurnishLabel", "Laske kalustuksen mukaan"},
                {"historyLabel", "Historia"},
                {"infoLabel", "Tiedot"},

                {"formTitle", "Syötä asunto"},
                {"formNameLabel", "Nimi:"},
                {"formNamePrompt", "Syötä asunnon nimi"},
                {"formXLabel", "X (Koordinaatit):"},
                {"formXPrompt", "Numero (max 497)"},
                {"formYLabel", "Y (Koordinaatit):"},
                {"formYPrompt", "Numero (> -738)"},
                {"formAreaLabel", "Pinta-ala:"},
                {"formAreaPrompt", "Pinta-ala (> 0)"},
                {"formRoomsLabel", "Huoneiden lukumäärä:"},
                {"formRoomsPrompt", "Huoneiden lkm (1-20)"},
                {"formFurnishLabel", "Kalustus:"},
                {"formViewLabel", "Näkymä:"},
                {"formTransportLabel", "Kuljetus:"},
                {"formHouseNameLabel", "Talon nimi:"},
                {"formHouseNamePrompt", "Talon nimi"},
                {"formHouseYearLabel", "Talon rakennusvuosi:"},
                {"formHouseYearPrompt", "Vuosi (> 0)"},
                {"formLiftsLabel", "Hissien lukumäärä:"},
                {"formLiftsPrompt", "Hissien lkm (> 0)"},
                {"formSaveButton", "Tallenna"},
                {"formCancelButton", "Peruuta"},

                {"authTitle", "Sisäänkirjautuminen"},
                {"authUsernamePrompt", "Käyttäjätunnus"},
                {"authPasswordPrompt", "Salasana"},
                {"authLoginButton", "Kirjaudu sisään"},
                {"authRegisterButton", "Rekisteröidy"},

                {"authStageTitle", "Flat Collector - Todennus"},
                {"mainStageTitle", "Flat Collector"},
        };
    }
}