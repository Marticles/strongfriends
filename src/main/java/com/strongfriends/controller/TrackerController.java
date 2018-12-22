package com.strongfriends.controller;

import com.alibaba.fastjson.JSONObject;
import com.strongfriends.model.HostHolder;
import com.strongfriends.model.Tracker;
import com.strongfriends.model.ViewObject;
import com.strongfriends.service.TrackerService;
import com.strongfriends.util.JedisAdapter;
import com.strongfriends.util.RedisKeyUtil;
import com.strongfriends.util.StrongFriendsUtil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class TrackerController {

    @Autowired
    TrackerService trackerService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/barpath"}, method = {RequestMethod.GET})
    public String barPath(Model model) {
        List<ViewObject> trackerVOs = getTrackerVOs();
        model.addAttribute("trackers", trackerVOs);
        return "barpath";
    }

    @RequestMapping(path = {"/barpath"}, method = {RequestMethod.POST})
    public String uploadBarPath(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        String fileName = file.getOriginalFilename();
        File saveFile = new File(StrongFriendsUtil.VIDEO_DIR + "/" + fileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            System.out.println(e);
        }
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(saveFile);
        ff.start();
        int length = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        f = ff.grabImage();
        String imgFilePath = StrongFriendsUtil.IMAGE_DIR + "/" + fileName + ".jpg";
        File imgFile = new File(imgFilePath);
        ImageIO.write(FrameToBufferedImage(f), "jpg", imgFile);
        ff.flush();
        ff.stop();
        model.addAttribute("file_name", fileName);
        model.addAttribute("upload_status", "上传视频成功，请框选杠铃位置");
        model.addAttribute("img_url", StrongFriendsUtil.STRONGFRIENDS_DOMAIN + "/image?name=" + fileName + ".jpg");
        List<ViewObject> trackerVOs = getTrackerVOs();
        model.addAttribute("trackers", trackerVOs);

        Tracker tracker = new Tracker();
        tracker.setUserId(hostHolder.getUser().getId());
        tracker.setOriginVideoName(fileName);
        tracker.setOriginImgName(fileName + ".jpg");
        tracker.setStatus(0);
        tracker.setCreatedDate(new Date());
        trackerService.addTracker(tracker);
        return "barpath";
    }

    @RequestMapping(path = {"/addtracktask"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addTrackTask(HttpServletRequest request) {
        LinkedHashMap<String, String> trackTaskMap = new LinkedHashMap<String, String>();
        if (hostHolder.getUser() == null) {
            return StrongFriendsUtil.getJSONString(1, "未登录无法使用该功能！");
        } else {
            trackTaskMap.put("user_id", String.valueOf(hostHolder.getUser().getId()));
            trackTaskMap.put("file_name", request.getParameter("file_name"));
            trackTaskMap.put("video_width", request.getParameter("video_height"));
            trackTaskMap.put("video_height", request.getParameter("video_width"));
            trackTaskMap.put("start_x", request.getParameter("start_x"));
            trackTaskMap.put("start_y", request.getParameter("start_y"));
            trackTaskMap.put("select_height", request.getParameter("select_height"));
            trackTaskMap.put("select_width", request.getParameter("select_width"));
            String json = JSONObject.toJSONString(trackTaskMap);
            String key = RedisKeyUtil.getTrackTaskKey();
            jedisAdapter.lpush(key, json);
            return StrongFriendsUtil.getJSONString(0, json);
        }
    }

    @RequestMapping(path = {"/trackerimg"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getTrackerImage(@RequestParam("name") String imageName,
                                HttpServletResponse response) {
        try {
            response.setContentType("image");
            StreamUtils.copy(new FileInputStream(new
                    File(StrongFriendsUtil.TRACKER_IMAGE_DIR + "/" + imageName)), response.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @RequestMapping(path = {"/trackerfile"}, method = {RequestMethod.GET})
    public void getTrackerFile(@RequestParam("name") String videoName,
                               HttpServletResponse response) throws IOException {
        String fileName = videoName + ".avi";
        File file = new File(StrongFriendsUtil.TRACKER_VIDEO_DIR + "/" + fileName);
        InputStream in = new FileInputStream(file);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        // 将FileInputStream流写入到buffer缓冲区
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);

        while ((len = in.read(buffer)) > 0) {
            // 将缓冲区的数据输出到浏览器
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();

    }


    public static BufferedImage FrameToBufferedImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public List<ViewObject> getTrackerVOs(){
        List<Tracker> trackers = trackerService.getById(hostHolder.getUser().getId());
        List<ViewObject> trackerVOs = new ArrayList<ViewObject>();
        for(Tracker track : trackers){
            ViewObject vo = new ViewObject();
            vo.set("id", track.getId());
            vo.set("origin_video_name", track.getOriginVideoName());
            vo.set("origin_img_name", track.getOriginImgName());
            vo.set("status", track.getStatus());
            vo.set("tracker_video_name", track.getTrackerVideoName());
            vo.set("tracker_img_name", track.getTrackerImgName());
            vo.set("created_date", track.getCreatedDate());
            trackerVOs.add(vo);
        }
        return trackerVOs;
    }


}

