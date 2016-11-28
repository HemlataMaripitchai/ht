package dumplingyzr.hearthtracker;

import android.os.AsyncTask;
import android.support.v7.util.SortedList;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by dumplingyzr on 2016/11/16.
 */

public class CardAPI {
    private static String sLocale;
    private static String sCardsJson;
    private static Object sLock = new Object();
    private static ArrayList<Card> sCardsById = new ArrayList<>();
    private static SortedList<Card> sCardsCollectible;
    private static HashMap<String, Card> sCardsByName = new HashMap<>();
    private static boolean sCardsReady;
    
    public void init(){
        sLocale = "enUS";
        sCardsCollectible = new SortedList<Card>(Card.class, new SortedList.Callback<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.cost.compareTo(c2.cost);
            }

            @Override
            public void onInserted(int position, int count) {
                return;
            }

            @Override
            public void onRemoved(int position, int count) {
                return;
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                return;
            }

            @Override
            public void onChanged(int position, int count) {
                return;
            }

            @Override
            public boolean areContentsTheSame(Card c1, Card c2) {
                // return whether the items' visual representations are the same or not.
                return c1.id.equals(c2.id);
            }

            @Override
            public boolean areItemsTheSame(Card c1, Card c2) {
                return c1.id.equals(c2.id);
            }
        });
        new GetCardsTask().execute();
    }

    public static boolean isReady(){
        return sCardsReady;
    }

    public static Card getCardByName(String name) {
        synchronized (sLock) {
            return sCardsByName.get(name);
        }
    }

    public static Card getCardById(String key) {
        synchronized (sLock) {
            if (sCardsById == null) {
                return Card.unknown();
            }
            int index = Collections.binarySearch(sCardsById, key);
            if (index < 0) {
                return Card.unknown();
            } else {
                return sCardsById.get(index);
            }
        }
    }

    public static Card getRandomCard() {
        synchronized (sLock) {
            if (sCardsById == null) {
                return Card.unknown();
            }
            int index = (int)(Math.random() * sCardsById.size());
            if (index < 0) {
                return Card.unknown();
            } else {
                return sCardsById.get(index);
            }
        }
    }

    private class GetCardsTask extends AsyncTask<Void, Void, String> {
        String endpoint = "https://api.hearthstonejson.com/v1/latest/" + sLocale + "/cards.json";
        Request request = new Request.Builder().url(endpoint).get().build();
        Response response = null;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                response = new OkHttpClient().newCall(request).execute();
                return new String(response.body().bytes());
            } catch (IOException e) {
                return "Unable to retrieve cards";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            sCardsJson = result;
            storeCards();
        }
    }

    private static void storeCards() {
        synchronized (sLock) {
            ArrayList<Card> list = new Gson().fromJson(sCardsJson, new TypeToken<ArrayList<Card>>() {
            }.getType());
            Collections.sort(list, new Comparator<Card>(){
                public int compare(Card c1, Card c2)
                {
                    return c1.id.compareTo(c2.id);
                }
            });
            sCardsById = list;
            for (Card c:list){
                sCardsByName.put(c.name, c);
                if(c.collectible.equals(true) && !c.type.equals("HERO")) sCardsCollectible.add(c);
            }
        }
        sCardsReady = true;
    }

    public static SortedList<Card> getsCardsCollectible() {
        return sCardsCollectible;
    }
}
