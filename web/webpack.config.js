const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const Dotenv = require('dotenv-webpack');
const path = require('path');

const mode = process.env.NODE_ENV || 'development';
const prod = mode === 'production';

module.exports = {
    entry: {
        bundle: ['./src/main.js']
    },
    resolve: {
        alias: {
            svelte: path.resolve('node_modules', 'svelte')
        },
        extensions: ['.mjs', '.js', '.svelte'],
        mainFields: ['svelte', 'browser', 'module', 'main']
    },
    output: {
        path: __dirname + '/public',
        filename: '[name].js',
        chunkFilename: '[name].[id].js'
    },
    module: {
        rules: [
            {
                test: /\.svelte$/,
                use: {
                    loader: 'svelte-loader',
                    options: {
                        emitCss: true,
                        hotReload: true
                    }
                }
            },
            {
                test: /\.css$/,
                use: [
                    /**
                     * MiniCssExtractPlugin doesn't support HMR.
                     * For developing, use 'style-loader' instead.
                     * */
                    prod ? MiniCssExtractPlugin.loader : 'style-loader',
                    'css-loader'
                ]
            }
        ]
    },
    mode,
    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].css'
        }),
        new Dotenv()
    ],
    devtool: prod ? false : 'source-map',
    devServer: {
        contentBase: path.join(__dirname, "public"),
        historyApiFallback: true,
        proxy: {
            "/api/*": {
                changeOrigin: true,
                secure: false,
                target: "https://euw1.api.riotgames.com/",
                onProxyReq: function onProxyReq(proxyReq, req, res) {
                    proxyReq.removeHeader("origin")
                },
                pathRewrite: {
                    '^/api': ''
                },
            },
            "/store/*": {
                changeOrigin: true,
                secure: false,
                target: "http://localhost:8080/",
                onProxyReq: function onProxyReq(proxyReq, req, res) {
                    proxyReq.removeHeader("origin")
                },
                pathRewrite: {
                    '^/store': ''
                },
            },
        },
    }
};
