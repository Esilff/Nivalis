package nivalis.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Esilff
 */

public class Shader {
    private int program;
    private int vertexShader;
    private int fragmentShader;
    public String[] shaders;

    /**
     * Create the program, the vertex shader, the fragment shader, then attach the two shaders to the program. Bind the
     * attributes and end by linking and validating the program. Can be used to display GLSL shaders on a model but also
     * to pass textures with the texture attribute.
     * @param filepath the path of the vertex and fragment shader.
     */

    public Shader(String filepath) throws ShaderException {
        shaders = new String[]{"",""};
        readShader(filepath);
        program = glCreateProgram();
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, shaders[0]);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new ShaderException("Failed to compile vertex shader : " + filepath);
        }
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, shaders[1]);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new ShaderException("Failed to compile fragment shader : " + filepath);
        }
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        int errorCheckValue = glGetError();
        if(errorCheckValue != GL_NO_ERROR)
        {
            throw new IllegalStateException("" + errorCheckValue);
        }
    }

    /**
     * Bind the shader to the model. This line is important since it's used to actually render the content of the shader
     * at the screen.
     */

    public void bind() {
        glUseProgram(program);

        int errorCheckValue = glGetError();
        if(errorCheckValue != GL_NO_ERROR)
        {
            throw new IllegalStateException("" + errorCheckValue);
        }
    }

    /**
     * Get the location and update the value of Integer uniform. It's mainly used to assign a texture to the shader.
     * @param name The name of the uniform.
     * @param value The integer value to put in the uniform.
     */

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) glUniform1i(location, value);
    }

    /**
     * Get the location and update the value of a Vector4f uniform. It's mainly used to affect the color of a shader.
     * @param name The name of the uniform.
     * @param value The matrix to put in the uniform.
     */

    public void setUniform(String name, Vector4f value) {
        int location = glGetUniformLocation(program, name);
        if (location != 1) glUniform4f(location, value.x, value.y, value.z, value.w);
    }


    /**
     * Get the location and update the value of Matrix4f uniform. It's mainly used to change the position, scale, and rotation
     * of the model binded to the shader.
     * @param name The name of the uniform.
     * @param value The matrix to put in the uniform.
     */

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1) glUniformMatrix4fv(location, false, buffer);
    }

    /**
     * Reads the content of a file and put it in a String[2] tab representing vertex and fragment shaders. It is used to
     * get the content of shader files since there is no file extension for vertex and fragment shaders.
     * @param filename The file that we want to read.
     */

    public void readShader(String filename) {
        int index = -1;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("#vertexshader") || line.contains("#fragmentshader")) index++;
                else {
                    shaders[index] += line + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
