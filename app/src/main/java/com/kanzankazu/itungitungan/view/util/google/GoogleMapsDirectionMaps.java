package com.kanzankazu.itungitungan.view.util.google;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleMapsDirectionMaps {

    private Activity activity;
    private ProgressDialog progressDialog;
    private String GOOGLE_MAPS_KEY_ID;

    public GoogleMapsDirectionMaps(Activity activity,String GOOGLE_MAPS_KEY_ID) {
        this.activity = activity;
        this.GOOGLE_MAPS_KEY_ID = GOOGLE_MAPS_KEY_ID;
    }

    /*ROUTE*/
    //GET DURATION AND LENGHT
    public void getDirection(LatLng mylatlng, LatLng marklatlng) {
        String url = getDirectionsUrl(mylatlng, marklatlng);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

        progressDialog = ProgressDialog.show(activity, "Loading...", "Please Wait.. Still get direction data", false, false);
    }

    private String getDirectionsUrl(LatLng mylatlng, LatLng marklatlng) {
        // Origin of route
        String str_origin = "origin=" + mylatlng.latitude + "," + mylatlng.longitude;
        // Destination of route
        String str_dest = "destination=" + marklatlng.latitude + "," + marklatlng.longitude;
        // Sensor enabled
        //avoid
        String avoid = "avoid=tolls|highways|ferries";
        //String sensor = "sensor=false";
        String sensor = "key=" + GOOGLE_MAPS_KEY_ID;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + avoid + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Snackbar.make(activity.findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG).show();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Lihat", "doInBackground DownloadTask : " + e.getMessage());
            }
            return data;
        }
        // Executes in UI thread, after the execution of

        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            progressDialog.dismiss();

            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;
            Polyline polylinefinal = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            if (polylinefinal != null) {
                polylinefinal.remove();
            }
            if (result != null) {
                if (result.size() < 1) {
                    Toast.makeText(activity.getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(activity.getBaseContext(), "Jarak dan WaktuModel tidak terdeteksi \n Tekan ulang tanda di atas mapsnya!", Toast.LENGTH_SHORT).show();
                    //fabrefresh.setVisibility(View.VISIBLE);
                    return;
                }
            }
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                polylineOptions.addAll(points);
                polylineOptions.width(5);
                polylineOptions.color(Color.RED);
            }
            //lldisdurfvbi.setVisibility(View.VISIBLE);
            //pbMapsMarkerfvbi.setVisibility(View.GONE);
            //tvDistancedaganganInfoMarkerfvbi.setText("Jarak : " + distance);
            //tvDurationdaganganInfoMarkerfvbi.setText("waktu : " + duration);
            // Drawing polyline in the Google Map for the i-th route
            //polylinefinal = mMap.addPolyline(polylineOptions);
        }
    }

    class DirectionsJSONParser{
        /**
         * Receives a JSONObject and returns a list of lists containing latitude and longitude
         */
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            JSONObject jDistance = null;
            JSONObject jDuration = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        /** Getting distance from the json data */
                        jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                        HashMap<String, String> hmDistance = new HashMap<String, String>();
                        hmDistance.put("distance", jDistance.getString("text"));
                        /** Getting duration from the json data */
                        jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                        HashMap<String, String> hmDuration = new HashMap<String, String>();
                        hmDuration.put("duration", jDuration.getString("text"));
                        /** Adding distance object to the path */
                        path.add(hmDistance);
                        /** Adding duration object to the path */
                        path.add(hmDuration);
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                    }
                    routes.add(path);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
            return routes;
        }

        /**
         * Method to decode polyline points
         * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         */
        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;
                LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }


}
