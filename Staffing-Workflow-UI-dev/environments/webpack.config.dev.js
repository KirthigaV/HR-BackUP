import webpack from 'webpack';
import ExtractTextPlugin from 'extract-text-webpack-plugin';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import path from 'path';

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
    devtool: 'eval-source-map', // more info:https://webpack.github.io/docs/build-performance.html#sourcemaps and https://webpack.github.io/docs/configuration.html#devtool
    noInfo: true, // set to false to see a list of every file being bundled.
    entry: [
        // must be first entry to properly set public path
        'webpack-public-path',
        'webpack-hot-middleware/client?reload=true',
        path.resolve(__dirname, '../src/index.js') // Defining path seems necessary for this to work consistently on Windows machines.
    ],
    target: 'web', // necessary per https://webpack.github.io/docs/testing.html#compile-and-test
    output: {
        path: path.resolve(__dirname, '../dist'), // Note: Physical files are only output by the production build task `npm run build`.
        publicPath: '/',
        filename: 'bundle.js'
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': JSON.stringify('development'), // Tells React to build in either dev or prod modes. https://facebook.github.io/react/downloads.html (See bottom)
            __DEV__: true
        }),
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoErrorsPlugin(),
        new ExtractTextPlugin('[name].[contenthash].css'),
        new HtmlWebpackPlugin({     // Create HTML file that includes references to bundled CSS and JS.
            template: 'src/index.ejs',
            minify: {
                removeComments: true,
                collapseWhitespace: true
            },
            inject: true
        })
    ],
    module: {
        loaders: [
            { test: /\.jsx?$/, exclude: /node_modules/, loaders: ['babel'] },
            {
                test: /\.woff(2)?(\?[a-z0-9]+)?$/,
                loader: "url-loader?limit=10000&mimetype=application/font-woff"
            },
            {
                test: /\.(ttf|eot|svg)(\?[a-z0-9]+)?$/,
                loader: "file-loader"
            },
            { test: /\.(jpe?g|png|gif)$/i, loader: 'file?name=[name].[ext]' },
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
