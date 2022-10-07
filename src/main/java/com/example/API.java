package com.example;

import java.net.URL;

import com.example.jsonobjects.gameFinished;
import com.example.jsonobjects.gameStarted;
import com.example.jsonobjects.getCard;
import com.example.jsonobjects.getState;
import com.example.jsonobjects.isTurn;
import com.example.jsonobjects.joinLobby;
import com.example.jsonobjects.sendAction;
import com.example.loveletter.Action;
import com.example.loveletter.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class API {

    private String token;

    public API(String agentToken) {
        this.token = agentToken;
    }

    // POST
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
    public getState get_state() throws IOException {
        try {
            // Sets up connection parameters
            String output = "http://localhost:8080/api/state?agentToken=" + this.token;
            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates input stream and converts to json object
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            getState json = mapper.readValue(responseStream, getState.class);

            return json;
            // Creates states object
            // Check json response and see if game has been started.
            // if (json.state == null) {
            // throw new Error("state received was null");
            // } else {
            // return json.state;
            // }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Lost Connection");
        }
    }

    // POST
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
            String actionName = String.format("play%s", action.card().toString());
            ObjectNode rootNode = mapperRequest.createObjectNode();
            rootNode.put("agentToken", this.token);
            rootNode.put("action", actionName);
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
            sendAction json = mapper.readValue(responseStream, sendAction.class);

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
    /* DEPRECATED */
    public getCard request_method(String method) throws IOException {
        try {
            // Sets up connection parameters
            URL url = new URL("http://localhost:8080/api/method");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Creates Json Body for request
            ObjectMapper mapperRequest = new ObjectMapper();
            ObjectNode rootNode = mapperRequest.createObjectNode();
            rootNode.put("agentToken", this.token);
            rootNode.put("keys", "null");
            rootNode.put("method", "getTopCard");
            rootNode.put("params", "null");
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
            getCard json = mapper.readValue(responseStream, getCard.class);

            return json;

        } catch (IOException e) {
            throw new IOException("connetion refused");
        }
    }
}