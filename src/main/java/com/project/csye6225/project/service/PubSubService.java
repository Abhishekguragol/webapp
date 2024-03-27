package com.project.csye6225.project.service;

import com.google.api.core.ApiFuture;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PubSubService {

    // Logger for logging  information about requests made by users
    Logger infoLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");

    // Logger for logging  information about requests made by users
    Logger debugLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");
    private final static String projectId = "csye6225-dev-414923";
    private final static String topicId = "verify_email";
    private final static String credentialsPath = "/opt/pubsub-service-account-key.json";

    public void publishPubSubMessage(String username) throws IOException, ExecutionException, InterruptedException {
    TopicName topicName = TopicName.of(projectId, topicId);
    Publisher publisher = null;

    try {
      Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
      // Create a publisher instance with default settings bound to the topic
      publisher = Publisher.newBuilder(topicName).setCredentialsProvider(() -> credentials).build();

      String message = "User Created: " + username;

      JsonObject jsonMessage = new JsonObject();
      jsonMessage.addProperty("username", username);
      jsonMessage.addProperty("message", message);

      Gson gson = new Gson();
      String gsonMessage = gson.toJson(jsonMessage);

      debugLogger.debug("PubbSub Debug: JSON message:"+gsonMessage);

      ByteString data = ByteString.copyFromUtf8(gsonMessage);
      PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                                                  .setData(data)
                                                  .putAttributes("username", username)
                                                  .build();

      // Once published, returns a server-assigned message id (unique within the topic)
      ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
      String messageId = messageIdFuture.get();
      System.out.println("Published message ID: " + messageId);
      infoLogger.info("PubSUB message INFO: message created with ID "+ messageId);
    } finally {
      if (publisher != null) {
        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();
        publisher.awaitTermination(1, TimeUnit.MINUTES);
      }
    }
  }
}