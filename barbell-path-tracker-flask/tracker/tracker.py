import cv2
import dlib
import os
from collections import deque
import redis
import pymysql
from config import secure

from concurrent.futures import ProcessPoolExecutor

pool = redis.ConnectionPool(host=secure.REDIS_HOST, port=secure.REDIS_PORT, decode_responses=True)
r = redis.Redis(connection_pool=pool)

speed = 50  # 播放速度
box_color = (255, 255, 255)
path_color = (0, 0, 255)
executor = ProcessPoolExecutor(5)

host = secure.DB_HOST
port = secure.DB_PORT
user = secure.DB_USERNAME
password = secure.DB_PASSWORD
db = secure.DB_NAME
charset = secure.DB_CHARSET


def load_task():
    redis_task = r.brpop("TRACK_TASK", 5)
    if redis_task:
        task = redis_task[1]
        task = eval(task)
        conn = pymysql.connect(host=host, port=port, user=user, password=password, db=db, charset=charset)
        cur = conn.cursor()
        sql = 'update tracker set status = 1 ,tracker_video_name = %s ,tracker_img_name= %s where origin_video_name = %s'
        cur.execute(sql, (task.get("file_name"), task.get("file_name"), task.get("file_name")))
        conn.commit()
        cur.close()
        conn.close()
        start_track(task)


def start_track(task):
    file_name = task.get("file_name")
    video_height = int(task.get("video_height"))
    video_width = int(task.get("video_width"))
    start_x = task.get("start_x")
    start_y = task.get("start_y")
    select_height = task.get("select_height")
    select_width = task.get("select_width")
    track(file_name, video_height, video_width, int(start_x), int(start_y), int(start_x) + int(select_width),
          int(start_y) + int(select_height))

def track(file_name, video_height, video_width, start_x, start_y, end_x, end_y):
    video_size = (video_height, video_width)
    video_path = os.path.join("D:/upload/video/" + file_name)  # 打开视频
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        return
    fourcc = cv2.VideoWriter_fourcc(*"MPEG")
    out = cv2.VideoWriter('D:/upload/track/video/' + file_name + '.avi', fourcc, 20, video_size)  # 保存轨迹视频路径
    tracker = dlib.correlation_tracker()
    frames_count = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    points = deque(maxlen=frames_count)

    i = 0
    for f in range(frames_count):
        timer = cv2.getTickCount()
        ret, frame = cap.read()
        if not ret:
            break
        img_raw = frame
        image = cv2.resize(img_raw.copy(), video_size, interpolation=cv2.INTER_CUBIC)

        if i == 0:
            tracker.start_track(image, dlib.rectangle(start_x, start_y, end_x, end_y))
        else:
            tracker.update(image)

        tracker_box = tracker.get_position()
        cv2.rectangle(image, (int(tracker_box.left()), int(tracker_box.top())),
                      (int(tracker_box.right()), int(tracker_box.bottom())), box_color, 1)
        center_point_x = int(int(tracker_box.left()) + 0.5 * int(tracker_box.width()))
        center_point_y = int(int(tracker_box.top()) + 0.5 * int(tracker_box.height()))
        center = (center_point_x, center_point_y)
        points.appendleft(center)
        cv2.circle(image, center, 2, path_color, -1)
        cv2.putText(image, "(X=" + str(center_point_x) + ",Y=" + str(center_point_y) + ")",
                    (int(tracker_box.right()), center_point_y), cv2.FONT_HERSHEY_SIMPLEX, 0.6, path_color, 2)
        fps = cv2.getTickFrequency() / (cv2.getTickCount() - timer)
        cv2.putText(image, "FPS: " + str(int(fps)), (20, 20), cv2.FONT_HERSHEY_SIMPLEX, 0.75, path_color, 2)

        for i in range(1, len(points)):
            if points[i - 1] is None or points[i] is None:
                continue
            cv2.line(image, points[i - 1], points[i], path_color, 2)
        i += 1
        out.write(image)
        if i == frames_count:
            cv2.imwrite('D:/upload/track/img/' + file_name + '.jpg', image)  # 保存最终轨迹图片
