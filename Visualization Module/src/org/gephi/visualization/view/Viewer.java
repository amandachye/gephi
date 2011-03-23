/*
Copyright 2008-2010 Gephi
Authors : Antonio Patriarca <antoniopatriarca@gmail.com>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.gephi.visualization.view;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Component;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 *
 *
 * @author Antonio Patriarca <antoniopatriarca@gmail.com>
 */
public class Viewer implements GLEventListener {
    
    private GLWindow window;
    private NewtCanvasAWT canvas;
    private FPSAnimator animator;

    private float angle = 0.0f;
    private int width;
    private int height;

    public Viewer() {
        final GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        // TODO: change capabilities based on config files

        this.window = GLWindow.create(caps);
        this.window.setAutoSwapBufferMode(true);

        this.animator = new FPSAnimator(this.window, 30);

        this.canvas = new NewtCanvasAWT(this.window);
    }
    
    public Component getCanvas() {
        return this.canvas;
    }

    public void start() {
        this.window.addGLEventListener(this);
        this.window.setVisible(true);
        this.canvas.setVisible(true);

        this.animator.start();
    }

    public void stop() {
        this.animator.stop();

        this.window.removeGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable glad) {
        final GL gl = glad.getGL();

        // TODO: change initialization code based on config files

        gl.glClearColor(1.0f, 0.5f, 0.5f, 1.0f);
        gl.glClearDepth(1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {
        final GL gl = glad.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        GL2 gl2 = gl.getGL2();

        gl2.glColor3f(0.0f, 0.5f, 0.5f);

        gl2.glBegin(gl.GL_TRIANGLES);

        final float x = width * 0.5f;
        final float y = height * 0.5f;
        final float d = Math.min(width, height) * 0.25f;
        final float c = (float) Math.cos(angle);
        final float s = (float) Math.sin(angle);

        gl2.glVertex2f(x + d*c, y + s*c);
        gl2.glVertex2f(x - 0.5f*d*s, y + 0.5f*d*c);
        gl2.glVertex2f(x + 0.5f*d*s, y - 0.5f*d*c);

        gl2.glEnd();

        angle += Math.PI / 180.0f;

        gl2.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int w, int h) {
        final GL gl = glad.getGL();

        gl.glViewport(0, 0, w, h);

        GL2 gl2 = gl.getGL2();

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        GLUgl2 glu = new GLUgl2();
        glu.gluOrtho2D(0.0f, w, 0.0f, h);
    }
}
