/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.importtsv;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class importTSV {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    private String[] atributos;
    private String fileOrigen;
    private String fileDestino;
    private BufferedWriter writer_a;

    public importTSV(String origen) {

    }

    public importTSV(String origen, String destino) {
        this.fileOrigen = origen;
        this.fileDestino = destino;
    }

    public void setAtributos() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileOrigen))) {
            String line = br.readLine();
            this.atributos = line.split("\t");
        }

    }

    public void getAtributos() throws IOException {
        for (String atr : atributos) {
            System.out.print("| " + atr + " |");
        }
    }

    private void createJsonObject(String[] valores) throws IOException {
        if (valores.length != atributos.length) {
            System.err.println("Número de atributos incorrecto");
        } else {

            Gson gson = new Gson();
            HashMap<String, String> objetoJson = new HashMap();

            for (int i = 0; i < atributos.length; i++) {
                objetoJson.put(atributos[i], valores[i]);
            }
            String jsonString = gson.toJson(objetoJson);
            //System.out.println(jsonString);

            writeToFile(jsonString);

        }

    }

    private void writeToFile(String s) throws IOException {

        writer_a.append(s);
        writer_a.append(System.getProperty("line.separator"));

    }

    private boolean existsJsonFile(String destino) {
        Path file = Paths.get(destino);
        if (Files.exists(file, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            System.out.println("El archivo destino existe, ¿añadir al final los datos? S/n");
            return true;
        } else {
            return false;
        }

    }

    private void serialize(String destino) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileOrigen))) {
            String line = br.readLine();
            this.atributos = line.split("\t");

            Path file = Paths.get("Output.json");

            writer_a = Files.newBufferedWriter(file, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
            writer_a.append("[" + System.getProperty("line.separator"));

            line = br.readLine();
            while (line != null) {
                String[] valores = line.split("\t");
                createJsonObject(valores);
                line = br.readLine();
            }
            writer_a.append(System.getProperty("line.separator") + "]");
            writer_a.close();
        }
    }

    public static void main(String[] args) throws IOException {
        importTSV prueba = new importTSV("W:\\EscritorioDatos\\data.tsv", "");
        prueba.setAtributos();
        prueba.getAtributos();
        //prueba.serialize("Destino");

    }
}
