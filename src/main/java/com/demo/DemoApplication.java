package com.demo;

import io.getstream.client.Client;
import io.getstream.client.FlatFeed;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    public static final String YOUR_API_KEY = "kubww96ccv5b";
    public static final String YOUR_SECRET_KEY = "ac5pexfhb3y4hsn6uvutkzefrjj5rzzm5vwzygn9fdfaumdut7ncyhedcqfvt8zj";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.print("Start execution-----------");
        Client client = Client.builder(
                YOUR_API_KEY,
                YOUR_SECRET_KEY
        ).build();
        Token userToken = client.frontendToken("user123");

        FlatFeed chris = client.flatFeed("user", "chris");
// Add an Activity; message is a custom field - tip: you can add unlimited custom fields!
        chris.addActivity(Activity.builder()
                .actor("chris")
                .verb("add")
                .object("picture:10")
                .foreignID("picture:10")
                .extraField("message", "Beautiful bird!")
                .build());

// Create a following relationship between Jack's "timeline" feed and Chris' "user" feed:
        FlatFeed jack = client.flatFeed("timeline", "jack");
        jack.follow(chris).join();


// Read Jack's timeline and Chris' post appears in the feed:
        List<Activity> response = jack.getActivities(new Pagination().limit(10)).join();
        for (Activity activity : response) {
            log.info("Response: {}", activity);
        }

// Remove an Activity by referencing it's foreign_id
//        chris.removeActivityByForeignID("picture:10").join();


// Get 5 activities with id less than the given UUID (Faster - Recommended!)
        final List<Activity> activityList = jack.getActivities(new Filter().idLessThanEqual("a404f1d1-fa17-11ed-b31f-06e2927a70f6").limit(5)).join();

        for (Activity activity : activityList) {
            log.info("Activity :{}",activity);
        }

    }
}


