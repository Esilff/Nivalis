package nivalis.engine.render;

public class GenericShader {
    public static final String TEXTURE_SHADER ="#vertexshader\n" +
            "#version 330 core\n" +
            "\n" +
            "attribute vec3 vertices;\n" +
            "attribute vec2 texture;\n" +
            "\n" +
            "varying vec2 tex_Coords;\n" +
            "\n" +
            "uniform mat4 projection;\n" +
            "\n" +
            "void main() {\n" +
            " tex_Coords = texture;\n" +
            " gl_Position = projection * vec4(vertices, 1.0);\n" +
            "}\n" +
            "\n" +
            "#fragmentshader\n" +
            "#version 330 core\n" +
            "\n" +
            "uniform sampler2D sampler;\n" +
            "\n" +
            "varying vec2 tex_Coords;\n" +
            "void main() {\n" +
            "    gl_FragColor = texture2D(sampler, tex_Coords);\n" +
            "}";

    public static final String MONOCHROME_SHADER = "#vertexshader\n" +
            "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "uniform mat4 projection;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            " fColor = aColor;\n" +
            " gl_Position = projection * vec4(aPos, 1.0);\n" +
            "}\n" +
            "\n" +
            "#fragmentshader\n" +
            "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "varying vec4 v_Color;\n" +
            "void main() {\n" +
            "    gl_FragColor = fColor;\n" +
            "}";
}
