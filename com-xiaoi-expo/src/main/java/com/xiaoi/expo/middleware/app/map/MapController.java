package com.xiaoi.expo.middleware.app.map;

import com.xiaoi.expo.common.response.MapResult;
import com.xiaoi.expo.common.utils.DateFormatUtils;
import com.xiaoi.expo.common.utils.HttpClientUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/2914:51
 */
@Controller
@RequestMapping(value = "/interface/map/")
public class MapController {

    private static final String MAP_INDEX = "baidumap";

    @Value("${gaode.direction.driving}")
    String drivingUrl;

    @Value("${gaode.direction.walking}")
    String walkingUrl;

    @Value("${gaode.direction.integrated}")
    String integratedUrl;

    @Value("${gaode.place.searchUrl}")
    String searchUrl;

    @Value("${gaode.web.key}")
    String gaodeKey;

    @Value("${gaode.place.city}")
    String gaodeCity;

    @Value("${gaode.place.citylimit}")
    String gaodeCityLimit;

    @Value("${gaode.place.extensions}")
    String gaodeExtensions;

    @Value("${gaode.place.offset}")
    String gaodeOffset;

    @Value("${gaode.place.page}")
    String gaodePage;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String baiduMap(String startpoint, String endpoint, ModelMap modelMap){
        if(!StringUtils.isEmpty(startpoint) && !StringUtils.isEmpty(endpoint)){
            String[] starts = startpoint.split(",");
            String[] ends = endpoint.split(",");
            modelMap.put("startLng", Double.parseDouble(starts[0]));
            modelMap.put("startLat", Double.parseDouble(starts[1]));
            modelMap.put("endLng", Double.parseDouble(ends[0]));
            modelMap.put("endLat", Double.parseDouble(ends[1]));

        }
        return MAP_INDEX;
    }

    @RequestMapping(value = "direction", method = RequestMethod.GET)
    @ResponseBody
    public MapResult direction(String origin, String destination){
        if(StringUtils.isEmpty(origin) || StringUtils.isEmpty(destination)){
            return MapResult.error("请输入起点和终点经纬度");
        }
        try {
            String result = HttpClientUtils.get(drivingUrl + "?origin=" + origin + "&destination=" + destination + "&key=" + gaodeKey);

            JSONObject jsonpObject = JSONObject.fromObject(result);
            MapResult mapResult = MapResult.ok();
            mapResult.put("driving",parseResult(result, null));


            String walkResult = HttpClientUtils.get(walkingUrl + "?origin=" + origin + "&destination=" + destination + "&key=" + gaodeKey);
            mapResult.put("walk",parseResult(walkResult, null));

            String integratedUrlResult = HttpClientUtils.get(integratedUrl + "?origin=" + origin + "&destination=" + destination + "&key=" + gaodeKey + "&city=0851");
            mapResult.put("bus",parseResult(integratedUrlResult, "bus"));

            return mapResult;
        } catch (Exception e) {
            e.printStackTrace();
            return MapResult.error("");
        }
    }

    private Map<String, Object> parseResult(String result, String type){
        Map<String, Object> drivingMap = new HashMap<String, Object>();
        JSONObject jsonpObject = JSONObject.fromObject(result);
        if(jsonpObject != null && "1".equals(jsonpObject.get("status")) && jsonpObject.get("route") != null){
            if(jsonpObject.getJSONObject("route").get("taxi_cost") != null){
                Double cost = Double.parseDouble(jsonpObject.getJSONObject("route").getString("taxi_cost"));
                DecimalFormat df = new DecimalFormat("#");
                drivingMap.put("cost", df.format(cost));
            }
//            drivingMap.put("cost", jsonpObject.getJSONObject("route").get("taxi_cost") != null ? jsonpObject.getJSONObject("route").getString("taxi_cost").substring(0, jsonpObject.getJSONObject("route").getString("taxi_cost").indexOf("\\\.")));
            if("bus".equals(type)){
                if(jsonpObject.getJSONObject("route").getJSONArray("transits") != null && jsonpObject.getJSONObject("route").getJSONArray("transits").size() > 0 &&
                        jsonpObject.getJSONObject("route").getJSONArray("transits").getJSONObject(0).get("duration") != null){
                    JSONObject transitObj = jsonpObject.getJSONObject("route").getJSONArray("transits").getJSONObject(0);
                    String durationStr =transitObj.getString("duration");
                    drivingMap.put("duration", DateFormatUtils.getDuration(Integer.parseInt(durationStr)));
                    if(transitObj.getJSONArray("segments") != null && transitObj.getJSONArray("segments").size() > 0){
                        StringBuffer trafficSb = new StringBuffer();
                        for(Object obj : transitObj.getJSONArray("segments")){
                            JSONObject segmentObj = JSONObject.fromObject(obj);
                            if(segmentObj.get("bus") != null && segmentObj.getJSONObject("bus").get("buslines") != null && segmentObj.getJSONObject("bus").getJSONArray("buslines").size() > 0){
                                JSONObject busLine = segmentObj.getJSONObject("bus").getJSONArray("buslines").getJSONObject(0);
                                if(busLine.get("name") != null){
                                    trafficSb.append(busLine.get("name"));
                                    trafficSb.append("、");
                                }


                            }
                        }

                        drivingMap.put("lines", trafficSb.toString().substring(0, trafficSb.length() - 1));
                       /* JSONObject segmentObj = transitObj.getJSONArray("segments").getJSONObject(0);
                        if(segmentObj.get("bus") != null && segmentObj.getJSONObject("bus").get("buslines") != null && segmentObj.getJSONObject("bus").getJSONArray("buslines").size() > 0){
                            StringBuffer trafficSb = new StringBuffer();
                            for(Object obj :  segmentObj.getJSONObject("bus").getJSONArray("buslines")){
                                JSONObject jsonObject = JSONObject.fromObject(obj);
                                trafficSb.append(jsonObject.getString("name"));
                                trafficSb.append("、");
                            }
                            drivingMap.put("lines", trafficSb.toString().substring(0, trafficSb.length() - 2));
                        }*/

                    }
                }
            }else{
                if(jsonpObject.getJSONObject("route").getJSONArray("paths") != null && jsonpObject.getJSONObject("route").getJSONArray("paths").size() > 0 &&
                        jsonpObject.getJSONObject("route").getJSONArray("paths").getJSONObject(0).get("duration") != null){
                    String durationStr = jsonpObject.getJSONObject("route").getJSONArray("paths").getJSONObject(0).getString("duration");
                    drivingMap.put("duration",DateFormatUtils.getDuration(Integer.parseInt(durationStr)));
                    System.out.println(jsonpObject);
                }
            }


        }
        return drivingMap;
    }


    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public String getBusinessByFoodName(String foodName){
        try {
            String result = HttpClientUtils.get(searchUrl + "?keywords=" + foodName + "&city=" + gaodeCity + "&citylimit=" + gaodeCityLimit + "&key=" + gaodeKey + "&extensions=" + gaodeExtensions + "&offset=" + gaodeOffset + "&page=" + gaodePage);

            JSONObject jsonpObject = JSONObject.fromObject(result);
            return searchResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "navigate", method = RequestMethod.GET)
    public String navgate(String fmapId, String startX, String startY, Integer startFloor, String endX, String endY, Integer endFloor, ModelMap modelMap){
        modelMap.put("fmapId", fmapId);
        modelMap.put("startX", startX);
        modelMap.put("startY", startY);
        modelMap.put("startFloor", startFloor);
        modelMap.put("endX", endX);
        modelMap.put("endY", endY);
        modelMap.put("endFloor", endFloor);
        return "fengmap";
    }

    private String searchResult( String result ){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonpObject = JSONObject.fromObject(result);
        if(jsonpObject != null && jsonpObject.get("pois") != null && "1".equals(jsonpObject.get("status"))){
            for(Object obj : jsonpObject.getJSONArray("pois")){
                JSONObject js = JSONObject.fromObject(obj);

                //String photos = (String)js.get("photos");
                if( js.get("photos") != null){ //没有图片的商家不展示

                    JSONArray phs = JSONArray.fromObject(js.get("photos"));
                    //String photo = (String) phs.get("photo");
                    if( phs.size() > 0 ){
                        JSONObject jp = JSONObject.fromObject(phs.get(0));
                        jsonResult.put("name", js.get("name"));
                        jsonResult.put("address", js.get("address"));
                        jsonResult.put("location", js.get("location"));
                        jsonResult.put("photo", jp.get("url"));
                        jsonArray.add(jsonResult);
                    }

                }
            }
        }else {
            jsonArray.add(jsonResult.put("error", "请求失败"));
        }
        return jsonArray.toString();
    }

}
