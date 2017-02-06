package jpa; /**
 * Created by diawara on 26/01/17.
 */

/*import fr.istic.crm.domain.Tuteur;
import fr.istic.crm.service.dto.TuteurDTO;
import fr.istic.crm.service.impl.TuteurServiceImpl;
import org.junit.jupiter.api.Test;*/

import fr.istic.crm.domain.Diplome;
import fr.istic.crm.domain.Tuteur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportCSV {

    public ImportCSV(){

    }
    /**
     *@param file
     * @return Vrai si le fichier existe
     */
    public static boolean existFile(File file){
        return file.exists();
    }
    /**
     * @param file
     * @return Vrai si le fichier que tu veux traiter est un fichier simple ou un dossier(isDirectory())
     */
    public static boolean typeFichier(File file){
        return file.isFile();
    }
    /**
     * @return le chemin complet du fichier à traiter
     */
    public static File getFile(String cheminFichier){
        return  new File(cheminFichier);
    }
    public static void lectureFichier(File file){
        Tuteur tuteur = new Tuteur();
        try{
            InputStream ips=new FileInputStream(file);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
                System.out.println(ligne);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void main (String args[]){
        ImportCSV test = new ImportCSV();

        //  Fichier monfichier = new Fichier();
        //Je recupère mon fichier
        File fichier = getFile("/home/diawara/Documents/Tuteurs.csv");
        //Je teste si ce fichier donné en param existe bel et bien
        if(!existFile(fichier)){
            System.out.println("Le fichier "+ fichier.getName() + "n'exite pas !");
            System.exit(0);
        }else{//Au cas ou le chemin que j'ai donné pointe juste sur un répertoire
            if(!fichier.isFile() || fichier.isDirectory()){
                System.out.println("Le fichier "+ fichier.getName() + " est +tôt un répertoire !");
                System.exit(0);
            }else{
                System.out.println("Chemin fichier : " + fichier.getAbsolutePath());
                System.out.println("Nom du fichier à traiter: " + fichier.getName());
                System.out.println("----------------- ");
                lectureFichier(fichier);
            }
            EntityManagerHelper.beginTransaction();

            try {
                test.fillBase();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EntityManagerHelper.commit();
            EntityManagerHelper.closeEntityManager();
            EntityManagerHelper.closeEntityManagerFactory();

        }
    }

    private void fillBase() {
        Tuteur tuteur = new Tuteur();
        tuteur.setNom("Foursov");
        tuteur.setPrenom("Micka");
        tuteur.setTelephone("0606060606");
        tuteur.setMail("micka.foufour@irisa.fr");

        EntityManagerHelper.getEntityManager().persist(tuteur);
    }


}
