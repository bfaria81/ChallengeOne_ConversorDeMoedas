package br.com.conversordemoedas;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class Conversor {
    //API_KEY fornecida pelo site da exchangerate
    private static final String API_KEY = "a49ad69a83a33f828a51e734";
    private static final String API_URL_BASE = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    converterMoeda("USD", "ARS");
                    break;
                case 2:
                    converterMoeda("ARS", "USD");
                    break;
                case 3:
                    converterMoeda("USD", "BRL");
                    break;
                case 4:
                    converterMoeda("BRL", "USD");
                    break;
                case 5:
                    converterMoeda("USD", "CAD");
                    break;
                case 6:
                    converterMoeda("CAD", "USD");
                    break;
                case 7:
                    converterMoeda("USD", "EUR");
                    break;
                case 8:
                    converterMoeda("EUR", "USD");
                    break;
                case 9:
                    converterMoeda("USD", "CLP");
                    break;
                case 10:
                    converterMoeda("CLP", "USD");
                    break;
                case 11:
                    converterMoeda("USD", "JPY");
                    break;
                case 12:
                    converterMoeda("JPY", "USD");
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("***********************************************************************");
        System.out.println("Bem-vindo ao conversor de moedas! \nEscolha o tipo de moeda para conversão:\n");
        System.out.println("1) USD Dólar Americano ==> ARS Peso Argentino");
        System.out.println("2) ARS Peso Argentino ==> USD Dólar Americano");
        System.out.println("3) USD Dólar Americano ==> BRL Real");
        System.out.println("4) BRL Real ==> USD Dólar Americano");
        System.out.println("5) USD Dólar Americano ==> CAD Dólar Canadense");
        System.out.println("6) Dólar Canadense ==> USD Dólar Americano");
        System.out.println("7) USD Dólar Americano ==> EUR Euro");
        System.out.println("8) EUR Euro ==> USD Dólar Americano");
        System.out.println("9) USD Dólar Americano ==> CLP Peso Chileno");
        System.out.println("10) CLP Peso Chileno ==> USD Dólar Americano");
        System.out.println("11) USD Dólar Americano ==> JPY Yene");
        System.out.println("12) JPY yene ==> USD Dólar Americano");
        System.out.println("0) Encerrar");
        System.out.println("***********************************************************************");
        System.out.print("Digite o número da opção desejada:");
    }

    private static void converterMoeda(String moedaBase, String moedaAlvo) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o valor em " + moedaBase + " que deseja converter para " + moedaAlvo + ": ");
        double valor = scanner.nextDouble();

        // Esse trecho se comunica com a API conforme documentação
        String apiUrl = API_URL_BASE + "/pair/" + moedaBase + "/" + moedaAlvo;
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);

        try {
            // Esse trecho envia a solicitação e obtem resposta do servidor
            HttpResponse response = httpClient.execute(request);

            // Esse trecho verifica o código de status da resposta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Esse trecho lê a resposta como uma string
                String responseBody = EntityUtils.toString(response.getEntity());

                // Aqui é feito o Parse do JSON usando Gson
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

                double taxaConversao = jsonObject.get("conversion_rate").getAsDouble();
                double valorConvertido = valor * taxaConversao;

                System.out.println("Valor convertido de " + moedaBase + " para " + moedaAlvo + ": " + valorConvertido);

                // System.out.println(responseBody); //Trecho caso queira mostrar mais informações de conversão da API

                System.out.println("Deseja continuar? (S/N)");
                String continuarCotacao = scanner.next();
                if (!continuarCotacao.equalsIgnoreCase("S")){
                    System.out.println("Encerrando o programa...");
                    System.exit(0);
                }

            } else {
                System.err.println("Falha na solicitação. Código de status: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
