from flask import Flask
from tracker import tracker

app = Flask(__name__)

@app.route('/start_task')
def start_task():
    tracker.load_task()
    return 'Added tracking task'

if __name__ == '__main__':
    app.run()
