package com.citizendemo.citizenapi;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chaos {
    private Logger logger = LoggerFactory.getLogger(Chaos.class);
    private String [] domains = { "google.com","facebook.com","doubleclick.net","google-analytics.com","googlesyndication.com","youtube.com","twitter.com","scorecardresearch.com","microsoft.com","apple.com","yahoo.com","bluekai.com","rubiconproject.com","verisign.com","addthis.com","crashlytics.com","amazonaws.com","live.com","digicert.com","pubmatic.com","instagram.com","mathtag.com","gmail.com","linkedin.com","yahooapis.com","betrad.com","flurry.com","newrelic.com","yimg.com","youtube-nocookie.com","exelator.com","amazon.com","bing.com","skype.com","tubemogul.com","contextweb.com","chartbeat.com","jquery.com","criteo.com","optimizely.com","adsymptotic.com","adobe.com","msn.com","tapad.com","truste.com","t.co","avast.com","spotxchange.com","adtechus.com","hola.org","liverail.com","windows.com","burstnet.com","disqus.com","geotrust.com","admob.com","thawte.com","lijit.com","cloudflare.com","360yield.com","dropbox.com","simpli.fi","smartadserver.com","globalsign.com","mlnadvertising.com","chango.com","moatads.com","entrust.net","tribalfusion.com","viber.com","doubleverify.com","criteo.net","outbrain.com","yieldmanager.com","goo.gl","voicefive.com","media6degrees.com","tynt.com","advertising.com","wp.com","adroll.com","icloud.com","gravatar.com","appsflyer.com","blogger.com","taboola.com","legolas-media.com","afy11.net","hike.in","feedburner.com","bootstrapcdn.com","brilig.com","sharethis.com","flashtalking.com","mediaplex.com","imgur.com","blogspot.com","wikimedia.org","amung.us","flickr.com","utorrent.com","switchads.com","mozilla.org","exponential.com","abmr.net","nanigans.com","zenoviaexchange.com","mixpanel.com","mopub.com","statcounter.com","jwpltx.com","parse.com","ensighten.com","adtech.de","brightcove.com","reddit.com","visualrevenue.com","google.com.br","google.it","jumptap.com","interclick.com","globalsign.net","eyereturn.com","pointroll.com","googlevideo.com","virtualearth.net","gumgum.com","tumblr.com","teamviewer.com","insightexpressai.com","gemius.pl","oracle.com","sonobi.com","ebay.com","surveymonkey.com","superfish.com","google.com.vn","tapjoy.com","blogblog.com","skimresources.com","akamai.com","starfieldtech.com","btstatic.com","researchnow.com","conviva.com","hotmail.com","bittorrent.com","openbittorrent.com","duba.net","impact-ad.jp","netflix.com","netsolssl.com","appspot.com","vk.com","mozilla.com","yadro.ru","histats.com","netseer.com","creativecommons.org","vizu.com","youtu.be","kau.li","eyeota.net","weather.com","provenpixel.com","veruta.com","paypal.com","office365.com","simplereach.com","specificclick.net","digg.com","google.ca","dotomi.com","undertone.com","urbanairship.com","dtscout.com","imdb.com","mzstatic.com","alexa.com","fastly.net","baidu.com","brealtime.com","amazon.co.uk","outlook.com","chartboost.com","adrta.com","adcash.com","adsonar.com","zedo.com","demonii.com","vimeo.com","adventori.com","coull.com","mxptint.net","paypalobjects.com","umeng.com","dl-rms.com","visualwebsiteoptimizer.com","avg.com","wordpress.com","livefyre.com","tabwpm.us","wordpress.org","gravity.com","huffingtonpost.com","exoclick.com","pandora.com","aol.com","adcolony.com","adhigh.net","eset.com","trustwave.com","cnn.com","cxense.com","lfstmedia.com","xboxlive.com","vungle.com","dailymotion.com","king.com","pingdom.net","lenovomm.com","soundcloud.com","hlserve.com","inmobi.com","bbc.co.uk","kaspersky.com","norton.com","nytimes.com","liveperson.net","amazon.in","amazon.de","adotube.com","go.com","parsely.com","windowsphone.com","washingtonpost.com","google.co.uk","pswec.com","supercell.net","andomedia.com","wikimediafoundation.org","alenty.com","zergnet.com","sundaysky.com","mediawiki.org","flipboard.com","tinyurl.com","clkmon.com","adblockplus.org","mailchimp.com"};
    public void SpikeOutboundHttp () {
        try {
            for (int i = 0; i < domains.length; i++) {
                URL url = new URL("http://" + domains[i]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
            }
        }  catch (Exception e) {
            logger.warn("Provisioning failed for resource " + e.getMessage());
        }
    }
}
