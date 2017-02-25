package jpa;

/**
 * Created by diawara on 26/01/17.
 */

import fr.istic.crm.domain.*;
import fr.istic.crm.domain.enumeration.Sexe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class ImportCSV {

    public static final String PERSISTENCE_NAME = "dev"; // profiles available : dev or prod

    private static final String TUTEUR = "Tuteur";
    private static final String ETUDIANT = "Etudiant";
    private static final String FILIERE = "Filiere";
    private static final String PROMOTION = "Promotion";
    private static final String TAXE = "Taxe";
    private static final String PARTENARIAT = "Partenariat";
    private static final String CONVENTION_STAGE = "ConventionStage";
    private static final String DIPLOME = "Diplome";
    private static final String GROUPE = "Groupe";
    private static final String PROFESSIONNEL = "Professionnel";
    private static final String ENTREPRISE = "Entreprise";
    private static final String SITE = "Site";

    private static final String CSV_DELIMITER = ";";
    private static final String CSV_DIRECTORY = "csv/";
    private static final String CSV_EXT = ".csv";
    private static final boolean SKIP_HEADERS = true;

    private static final String YEAR_FORMAT = "yyyy";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static void main (String args[]){
        ImportCSV app = new ImportCSV();

        EntityManagerHelper.beginTransaction();

        app.readDirectory(CSV_DIRECTORY);

        EntityManagerHelper.commit();
        EntityManagerHelper.closeEntityManager();
        EntityManagerHelper.closeEntityManagerFactory();

    }

    private void readDirectory(String directory) {
        Path csvDirectory = Paths.get(directory);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(csvDirectory, "*" + CSV_EXT)) {
            for (Path file : stream) {
                File csvFile = new File(file.toString());
                lectureFichier(csvFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void lectureFichier(File fichier) {

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne = "";

            if (SKIP_HEADERS) ligne = br.readLine();

            while ((ligne=br.readLine())!=null){
                String[] field = ligne.split(CSV_DELIMITER);

                persistEntity(fichier, field);

            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void persistEntity(File fichier, String[] field) {
        if (fichier.getName().startsWith(TUTEUR)) {
            Tuteur tuteur = new Tuteur()
                    .nom(field[0])
                    .prenom(field[1])
                    .telephone(field[2])
                    .mail(field[3]);

            EntityManagerHelper.getEntityManager().persist(tuteur);
        }
        else if (fichier.getName().startsWith(ETUDIANT)) {
            Etudiant etudiant = new Etudiant()
                    .nom(field[0])
                    .prenom(field[1])
                    .mail(field[2])
                    .sexe(getSexe(field[3]))
                    .numEtudiant(field[4]);

            EntityManagerHelper.getEntityManager().persist(etudiant);

        }
        else if (fichier.getName().startsWith(FILIERE)) {
            Filiere filiere = new Filiere()
                    .niveau(field[0]);

            EntityManagerHelper.getEntityManager().persist(filiere);

        }
        else if (fichier.getName().startsWith(PROMOTION)) {
            Promotion promotion = new Promotion()
                    .annee(getZonedDateTime(field[0], YEAR_FORMAT));

            EntityManagerHelper.getEntityManager().persist(promotion);

        }
        else if (fichier.getName().startsWith(TAXE)) {
            Taxe taxe = new Taxe()
                    .montant(Double.valueOf(field[0]))
                    .annee(getZonedDateTime(field[1], YEAR_FORMAT));

            EntityManagerHelper.getEntityManager().persist(taxe);

        }
        else if (fichier.getName().startsWith(PARTENARIAT)) {
            Partenariat partenariat= new Partenariat()
                    .dateDebut(getZonedDateTime(field[0], DATE_FORMAT))
                    .dateFin(getZonedDateTime(field[1], DATE_FORMAT));

            EntityManagerHelper.getEntityManager().persist(partenariat);

        }
        else if (fichier.getName().startsWith(DIPLOME)) {
            Diplome diplome = new Diplome()
                    .nom(field[0])
                    .dateCreation(convertDateToLong())
                    .dateModification(convertDateToLong());

            EntityManagerHelper.getEntityManager().persist(diplome);

        }
        else if (fichier.getName().startsWith(GROUPE)) {
            Groupe groupe = new Groupe()
                    .nom(field[0])
                    .dateCreation(convertDateToLong())
                    .dateModification(convertDateToLong());

            EntityManagerHelper.getEntityManager().persist(groupe);

        }

        else if (fichier.getName().startsWith(ENTREPRISE)) {
            Entreprise entreprise = new Entreprise()
                    .nom(field[0])
                    .pays(field[1])
                    .numSiret(field[2])
                    .numSiren(field[3])
                    .telephone(field[4])
                    .dateCreation(convertDateToLong())
                    .dateModification(convertDateToLong());

            EntityManagerHelper.getEntityManager().persist(entreprise);

        }
//        else if (fichier.getName().startsWith(PROFESSIONNEL)) {
//            Professionnel professionnel = new Professionnel()
//                    .nom(field[0])
//                    .prenom(field[1])
//                    .telephone(field[2])
//                    .mail(field[3])
//                    .fonction(field[4])
//                    .ancienEtudiant(Boolean.valueOf(field[5]))
//                    .dateCreation(convertDateToLong())
//                    .dateModification(convertDateToLong());
//
//            EntityManagerHelper.getEntityManager().persist(professionnel);
//
//        }
//        else if (fichier.getName().startsWith(SITE)) {
//
//            Site site = new Site()
//                    .adresse(field[0])
//                    .codePostal(field[1])
//                    .ville(field[2])
//                    .pays(field[3])
//                    .telephone(field[4])
//                    .dateCreation(convertDateToLong())
//                    .dateModification(convertDateToLong());
//
//            EntityManagerHelper.getEntityManager().persist(site);
//
//        }
//        else if (fichier.getName().startsWith(CONVENTION_STAGE)) {
//
//            ConventionStage conventionStage = new ConventionStage()
//                    .sujet(field[0])
//                    .fonctions(field[1])
//                    .competences(field[2])
//                    .dateDebut(getZonedDateTime(field[3], DATE_FORMAT))
//                    .dateDebut(getZonedDateTime(field[4], DATE_FORMAT));
//
//            EntityManagerHelper.getEntityManager().persist(conventionStage);
//
//        }
    }

    private long convertDateToLong() {
        Date date = new Date();
        return (date.getTime());
    }

    private Sexe getSexe(String sexeInCsv) {

        return sexeInCsv.equals("FEMME") ? Sexe.FEMME : Sexe.HOMME;
    }

    private ZonedDateTime getZonedDateTime(String dateInCsv, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date date = null;
        ZonedDateTime zonedDateTime = null;

        try {
            date = formatter.parse(dateInCsv);
            zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        } catch (ParseException e) {
            e.printStackTrace();

        }

        return zonedDateTime;
    }

}
