package com.example;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.MediaType;

import com.example.jsonobjects.HttpGetWithEntity;
import com.example.jsonobjects.gameFinished;
import com.example.jsonobjects.gameStarted;
import com.example.jsonobjects.isTurn;
import com.example.jsonobjects.joinLobby;
import com.example.jsonobjects.sendActionResponse;
import com.example.jsonobjects.getAction;
import com.example.loveletter.Action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

/**
 * 
 */
public class API {

    private String token;

    /**
     * 
     * @param agentToken
     */
    public API(String agentToken) {
        this.token = agentToken;
    }

    // POST
    /**
     * 
     * @param gameID
     * @return
     * @throws IOException
     */
    public Boolean join_lobby(String gameID) throws IOException {
        try {
            // Sets up connection parameters
            URL url = new URL("http://localhost:8080/api/join");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates Json Body for request
            ObjectMapper mapperRequest = new ObjectMapper();
            ObjectNode rootNode = mapperRequest.createObjectNode();
            rootNode.put("agentToken", this.token);
            rootNode.put("gameID", gameID);
            String lobbyJSON = mapperRequest.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

            // Writes out the created json to the body
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(lobbyJSON);
            osw.flush();
            osw.close();
            os.close();
            connection.connect();

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            joinLobby json = mapper.readValue(responseStream, joinLobby.class);

            if (!json.success()) {
                return false;
            } else {
                return true;
            }

        } catch (IOException e) {
            throw new IOException("connetion refused at join_lobby()");
        }
    }

    // GET
    /**
     * 
     * @return
     * @throws IOException
     */
    public Boolean game_started() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/started?agentToken=" + this.token;
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            gameStarted json = mapper.readValue(responseStream, gameStarted.class);

            // Check json response and see if game has been started.
            if (json.started()) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection at game_started()");
        }
    }

    // GET
    /**
     * 
     * @return
     * @throws IOException
     */
    public Boolean game_finished() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/finished?agentToken=" + this.token;
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            gameFinished json = mapper.readValue(responseStream, gameFinished.class);

            // Check json response and see if game has been started.
            if (json.finished()) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection");
        }
    }

    // GET
    /**
     * 
     * @return
     * @throws IOException
     */
    public Boolean is_turn() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/turn?agentToken=" + this.token;
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            isTurn json = mapper.readValue(responseStream, isTurn.class);

            // Check json response and see if game has been started.
            if (json.turn()) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection");
        }
    }

    // GET
    /**
     * 
     * @return
     * @throws IOException
     */
    public JsonNode get_state() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/state2";
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(responseStream);
            return json;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection");
        }
    }

    // GET
    /**
     * 
     * @return
     * @throws IOException
     */
    public JsonNode get_action() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/getAction?agentToken=" + this.token;
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(responseStream);
            return json;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection");
        }
    }

    // POST
    /**
     * 
     * @param action
     * @return
     * @throws IOException
     */
    public Boolean send_action(Action action) throws IOException {
        try {
            // Sets up connection parameters
            URL url = new URL("http://localhost:8080/api/action");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates Json Body for request
            ObjectMapper mapperRequest = new ObjectMapper();
            ObjectNode rootNode = mapperRequest.createObjectNode();
            rootNode.put("agentToken", this.token);

            // Create Json string for agent variable in json response
            String actionName = String.format("play%s", action.card().toString());
            int[] params = new int[2];
            params[0] = action.player();
            params[1] = action.target();
            getAction actionjsonClass = new getAction(actionName, params);
            String actionJson = mapperRequest.writeValueAsString(actionjsonClass);

            // Assign actionJson to action variable in body
            rootNode.put("action", actionJson);
            String lobbyJSON = mapperRequest.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

            // Writes out the created json to the body
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(lobbyJSON);
            osw.flush();
            osw.close();
            os.close();
            connection.connect();

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            sendActionResponse json = mapper.readValue(responseStream, sendActionResponse.class);

            if (json.success == false) {
                return false;
            } else {
                return true;
            }

        } catch (IOException e) {
            throw new IOException("connetion refused");
        }
    }

    // GET
    /**
     * Repurposed to get the top card of the game of the state controller serverside
     * 
     * @param method
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */

    public JSONObject request_method(String method)
            throws ClientProtocolException, IOException, URISyntaxException, JSONException {
        JSONObject temp = null;
        try {
            // Sets up connection parameters
            ObjectMapper mapperRequest = new ObjectMapper();
            ObjectNode rootNode = mapperRequest.createObjectNode();
            rootNode.put("agentToken", this.token);
            rootNode.putNull("keys");
            rootNode.put("method", "getTopCard");
            rootNode.putNull("params");
            String lobbyJSON = mapperRequest.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            JSONObject lobbyObject = new JSONObject(lobbyJSON);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            URL url = new URL("http://localhost:8080/api/method");
            HttpRequest request = new HttpGetWithEntity(url.toURI(), lobbyObject);
            request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
            HttpResponse response;
            response = httpClient.execute(new HttpHost(url.getHost(), url.getPort()), request);
            temp = new JSONObject(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));

        } catch (Error e) {
            e.printStackTrace();
        }
        return temp;

    }
}
