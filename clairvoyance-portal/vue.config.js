const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin')
module.exports = {
    css: {
        loaderOptions: { // 向 CSS 相关的 loader 传递选项
            less: {
                javascriptEnabled: true
            }
        }
    },
    devServer: {
        port: 3098,
        open: true,
        overlay: {
            warnings: false,
            errors: true
        },
        proxy: {
            '/*': {
                ws: false,
                target: 'http://127.0.0.1:8891',
                // target: 'http://172.16.120.51:9533',
                pathRewrite: { '^/': '' },
                secure: false,
                changeOrigin: true
            }
        }
        // after: require('./mock/mock-server.js')
    },
    configureWebpack: {
        name: '统一查询平台',
        plugins: [
            new MonacoWebpackPlugin({
                languages: ['abap', 'apex', 'azcli', 'bat', 'cameligo', 'clojure', 'coffee', 'cpp', 'csharp', 'csp', 'css', 'dockerfile', 'fsharp', 'go', 'graphql', 'handlebars', 'html', 'ini', 'java', 'javascript', 'json', 'kotlin', 'less', 'lua', 'markdown', 'mips', 'msdax', 'mysql', 'objective-c', 'pascal', 'pascaligo', 'perl', 'pgsql', 'php', 'postiats', 'powerquery', 'powershell', 'pug', 'python', 'r', 'razor', 'redis', 'redshift', 'restructuredtext', 'ruby', 'rust', 'sb', 'scheme', 'scss', 'shell', 'solidity', 'sophia', 'sql', 'st', 'swift', 'tcl', 'twig', 'typescript', 'vb', 'xml', 'yaml']
            })
        ],
        module: {
            rules: [
                {
                    test: /\.(ts|tsx)?$/,
                    use: {
                        loader: 'ts-loader'
                    },
                    exclude: /node_modules/
                }
            ]
        }
    },
    transpileDependencies: [
        'vue-echarts',
        'resize-detector'
    ]
}
