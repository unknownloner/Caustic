/*
 * This file is part of Caustic.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Caustic is licensed under the Spout License Version 1.
 *
 * Caustic is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Caustic is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.renderer.lwjgl.gl20;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import org.spout.renderer.data.Color;
import org.spout.renderer.gl.Context;
import org.spout.renderer.gl.Texture.Format;
import org.spout.renderer.lwjgl.LWJGLUtil;
import org.spout.renderer.util.CausticUtil;
import org.spout.renderer.util.Rectangle;

/**
 * An OpenGL 2.0 implementation of {@link org.spout.renderer.gl.Context}.
 *
 * @see org.spout.renderer.gl.Context
 */
public class GL20Context extends Context {
	protected GL20Context() {
	}

	@Override
	public void create() {
		if (isCreated()) {
			throw new IllegalStateException("Context has already been created");
		}
		// Attempt to create the display
		try {
			Display.setDisplayMode(new DisplayMode(windowSize.getFloorX(), windowSize.getFloorY()));
			Display.create(new PixelFormat().withSamples(this.msaa), createContextAttributes());
		} catch (LWJGLException ex) {
			throw new IllegalStateException("Unable to create OpenGL context: " + ex.getMessage());
		}
		// Set the title
		Display.setTitle(this.windowTitle);
		// Set the default view port
		GL11.glViewport(0, 0, windowSize.getFloorX(), windowSize.getFloorY());
		// Set the alpha blending function for transparency
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// Check for errors
		LWJGLUtil.checkForGLError();
		// Update the state
		super.create();
	}

	/**
	 * Created new context attributes for the version.
	 *
	 * @return The context attributes
	 */
	protected ContextAttribs createContextAttributes() {
		return new ContextAttribs(2, 1);
	}

	@Override
	public void destroy() {
		checkCreated();
		// Display goes after else there's no context in which to check for an error
		LWJGLUtil.checkForGLError();
		Display.destroy();
		super.destroy();
	}

	@Override
	public void updateDisplay() {
		checkCreated();
		Display.update();
	}

	@Override
	public void setClearColor(Color color) {
		Color normC = color.normalize();
		GL11.glClearColor(normC.getRed(), normC.getGreen(), normC.getBlue(), normC.getAlpha());
		// Check for errors
		LWJGLUtil.checkForGLError();
	}

	@Override
	public void clearCurrentBuffer() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// Check for errors
		LWJGLUtil.checkForGLError();
	}

	@Override
	public void enableCapability(Capability capability) {
		GL11.glEnable(capability.getGLConstant());
		// Check for errors
		LWJGLUtil.checkForGLError();
	}

	@Override
	public void setViewPort(Rectangle viewPort) {
		GL11.glViewport(viewPort.getX(), viewPort.getY(), viewPort.getWidth(), viewPort.getHeight());
		// Check for errors
		LWJGLUtil.checkForGLError();
	}

	@Override
	public ByteBuffer readCurrentFrame(Rectangle size, Format format) {
		final ByteBuffer buffer = CausticUtil.createByteBuffer(size.getArea() * 3);
		GL11.glReadBuffer(GL11.GL_FRONT);
		GL11.glReadPixels(size.getX(), size.getY(), size.getWidth(), size.getHeight(), format.getGLConstant(), GL11.GL_UNSIGNED_BYTE, buffer);
		return buffer;
	}

	@Override
	public void disableCapability(Capability capability) {
		GL11.glDisable(capability.getGLConstant());
		// Check for errors
		LWJGLUtil.checkForGLError();
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL20;
	}
}
