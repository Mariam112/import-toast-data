package jpa;

/**
 * Created by diawara on 26/01/17.
 */

import fr.istic.crm.domain.Etudiant;
import fr.istic.crm.domain.Filiere;
import fr.istic.crm.domain.Tuteur;
import fr.istic.crm.domain.enumeration.Sexe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportCSV {

    public static final String PERSISTENCE_NAME = "dev"; // profiles available : dev or prod

    private static final String TUTEUR = "Tuteur";
    private static final String ETUDIANT = "Etudiant";
    private static final String FILIERE = "Filiere";

    private static final String CSV_DELIMITER = ";";
    private static final String CSV_DIRECTORY = "csv/";
    private static final String CSV_EXT = ".csv";
    private static final boolean SKIP_HEADERS = true;


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
    }

    private Sexe getSexe(String sexeInCsv) {

        return sexeInCsv.equals("FEMME") ? Sexe.FEMME : Sexe.HOMME;
    }

}
