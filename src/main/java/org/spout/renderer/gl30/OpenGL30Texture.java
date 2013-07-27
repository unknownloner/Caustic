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
package org.spout.renderer.gl30;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import org.spout.renderer.gl20.OpenGL20Texture;

/**
 * Represents a texture for OpenGL 3.0. The textures image, dimension, wrapping and filters must be
 * set before it can be created. This texture offers mipmap support using the OpenGL 3.0 feature.
 */
public class OpenGL30Texture extends OpenGL20Texture {
	@Override
	protected void uploadTexture(ByteBuffer buffer, int width, int height) {
		// Upload the texture
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format.getGLConstant(), width, height, 0, format.getGLConstant(), GL11.GL_UNSIGNED_BYTE, buffer);
		// Generate mipmaps if necessary
		if (minFilter.needsMipMaps()) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		}
	}
}