package org.openmaptiles.addons;

import com.onthegomap.planetiler.FeatureCollector;
import com.onthegomap.planetiler.reader.SourceFeature;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openmaptiles.Layer;
import org.openmaptiles.OpenMapTilesProfile;

public class Raw implements Layer, OpenMapTilesProfile.OsmAllProcessor {

  public static List<String> TAG_WHITELIST = Arrays.asList("amenity", "shop", "name", "opening_hours", "email", "website", "phone", "internet_access", "wheelchair", "cuisine", "indoor_seating", "outdoor_seating", "takeaway", "drive_through");
  public static List<String> TAG_PREFIX_WHITELIST = Arrays.asList("addr:", "contact:", "payment:", "wheelchair:", "fuel:", "internet_access:");

  @Override
  public String name() {
    return "raw";
  }

  @Override
  public void processAllOsm(SourceFeature feature, FeatureCollector features) {
    boolean hasTag = false;
    for (Map.Entry<String, Object> i : feature.tags().entrySet()) {
      if (TAG_WHITELIST.contains(i.getKey())) {
        hasTag = true;
        break;
      }
      for (String prefix : TAG_PREFIX_WHITELIST) {
        if(i.getKey().startsWith(prefix)) {
          hasTag = true;
          break;
        }
      }
    }

    if(!hasTag) return;

    if(feature.canBePolygon()) {
      FeatureCollector.Feature ft = features.polygon("raw")
          .setBufferPixels(4)
          .setMinZoom(15);

      for (Map.Entry<String, Object> i : feature.tags().entrySet()) {
        if (TAG_WHITELIST.contains(i.getKey())) {
          ft.setAttr(i.getKey(), i.getValue());
        }
        for (String prefix : TAG_PREFIX_WHITELIST) {
          if(i.getKey().startsWith(prefix)) {
            ft.setAttr(i.getKey(), i.getValue());
            break;
          }
        }
      }
    } else if(feature.canBeLine()) {
      FeatureCollector.Feature ft = features.line("raw")
          .setBufferPixels(4)
          .setMinZoom(15);

      for (Map.Entry<String, Object> i : feature.tags().entrySet()) {
        if (TAG_WHITELIST.contains(i.getKey())) {
          ft.setAttr(i.getKey(), i.getValue());
        }
        for (String prefix : TAG_PREFIX_WHITELIST) {
          if(i.getKey().startsWith(prefix)) {
            ft.setAttr(i.getKey(), i.getValue());
            break;
          }
        }
      }
    } else if(feature.isPoint()) {
      FeatureCollector.Feature ft = features.point("raw")
          .setBufferPixels(4)
          .setMinZoom(15);

      for(Map.Entry<String, Object> i : feature.tags().entrySet()) {
        if(TAG_WHITELIST.contains(i.getKey())) {
          ft.setAttr(i.getKey(), i.getValue());
        }
        for (String prefix : TAG_PREFIX_WHITELIST) {
          if(i.getKey().startsWith(prefix)) {
            ft.setAttr(i.getKey(), i.getValue());
            break;
          }
        }
      }
    }
  }
}
