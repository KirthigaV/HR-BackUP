// For info about this file refer to webpack and webpack-hot-middleware documentation
// For info on how we're generating bundles with hashed filenames for cache busting: https://medium.com/@okonetchnikov/long-term-caching-of-static-assets-with-webpack-1ecb139adb95#.w99i89nsz
import webpack from 'webpack';
import ExtractTextPlugin from 'extract-text-webpack-plugin';
import WebpackMd5Hash from 'webpack-md5-hash';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import path from 'path';

const GLOBALS = {
    'process.env.NODE_ENV': JSON.stringify('production'),
    __DEV__: false
};

export default {
    resolve: {
        extensions: ['', '.js', '.jsx', '.json'],
        modulesDirectories: [
            'config',
            'src',
            'environments',
            'node_modules'
        ]
    },
    debug: true,
    devtool: 'source-map', // more info:https://webpack.github.io/docs/build-performance.html#sourcemaps and https://webpack.github.io/docs/configuration.html#devtool
    noInfo: true, // set to false to see a list of every file being bundled.
    entry: path.resolve(__dirname, '../src/index.js'),
    target: 'web', // necessary per https://webpack.github.io/docs/testing.html#compile-and-test
    output: {
        path: path.resolve(__dirname, '../dist'),
        publicPath: '/',
        filename: '[name].[chunkhash].js'
    },
    plugins: [
        // Hash the files using MD5 so that their names change when the content changes.
        new WebpackMd5Hash(),

        // Optimize the order that items are bundled. This assures the hash is deterministic.
        new webpack.optimize.OccurenceOrderPlugin(),

        // Tells React to build in prod mode. https://facebook.github.io/react/downloads.html
        new webpack.DefinePlugin(GLOBALS),

        // Generate an external css file with a hash in the filename
        new ExtractTextPlugin('[name].[contenthash].css'),

        // Generate HTML file that contains references to generated bundles. See here for how this works: https://github.com/ampedandwired/html-webpack-plugin#basic-usage
        new HtmlWebpackPlugin({
            template: 'src/index.ejs',
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeRedundantAttributes: true,
                useShortDoctype: true,
                removeEmptyAttributes: true,
                removeStyleLinkTypeAttributes: true,
                keepClosingSlash: true,
                minifyJS: true,
                minifyCSS: true,
                minifyURLs: true
            },
            inject: true,
            // Note that you can add custom options here if you need to handle other custom logic in index.html
            // To track JavaScript errors via TrackJS, sign up for a free trial at TrackJS.com and enter your token below.
            trackJSToken: ''
        }),

        // Eliminate duplicate packages when generating bundle
        new webpack.optimize.DedupePlugin(),

        // Minify JS
        new webpack.optimize.UglifyJsPlugin({ sourceMap: true })
    ],
    module: {
        loaders: [
            { test: /\.jsx?$/, exclude: /node_modules/, loader: 'babel' },
            {
                test: /\.woff(2)?(\?[a-z0-9]+)?$/,
                loader: "url-loader?limit=10000&mimetype=application/font-woff"
            },
            {
                test: /\.(ttf|eot|svg)(\?[a-z0-9]+)?$/,
                loader: "file-loader"
            },
            { test: /\.(jpe?g|png|gif)$/i, loader: 'file?name=assets/[name].[ext]' },
            { test: /\.ico$/, loader: 'file?name=[name].[ext]' },
            {
                test: /(\.css|\.scss)$/,
                exclude: /node_modules/,
                loader: ExtractTextPlugin.extract('css?sourceMap!sass?sourceMap')
            },
            {
                test: /(\.scss|\.css)$/,
                include: /node_modules\/material-ui/,
                loader: ExtractTextPlugin.extract('style', 'css?sourceMap&modules&importLoaders=1&localIdentName=[name]__[local]___[hash:base64:5]!sass?sourceMap')
            },
            { test: /\.json$/, loader: "json" }
        ]
    },
    sassLoader: {
        data: '@import "./src/global-styles/_theme.scss";',
        includePaths: [path.resolve(__dirname, './src')]
    }
};
