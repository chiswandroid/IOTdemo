/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.washington.cs.touchfreelibrary.sensors;

import org.opencv.core.Point;

/**
 * Only used within the package to get data from JNI.
 *
 * @author Leeran Raphaely <leeran.raphaely@gmail.com>
 */
public class MotionDetectionReturnValue {
    public Point averagePosition;
    public double fractionOfScreenInMotion;
    double bottomRightFraction;
    double bottomLeftFraction;
    double topRightFraction;
    double topLeftFraction;
    double x;
    double y;

    public MotionDetectionReturnValue(double x, double y, double fraction, double bottomRightFraction, double bottomLeftFraction, double topRightFraction, double topLeftFraction) {
        averagePosition = new Point(x, y);
        fractionOfScreenInMotion = fraction;
        this.x = x;
        this.y = y;
        this.bottomRightFraction = bottomRightFraction;
        this.bottomLeftFraction = bottomLeftFraction;
        this.topRightFraction = topRightFraction;
        this.topLeftFraction = topLeftFraction;
    }
}
