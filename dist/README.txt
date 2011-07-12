README.txt
==========

This file describes how to use Kuromoji with Apache Solr.

Please follow these steps.


Step 1.  Download and unpack Apache Solr
----------------------------------------

Solr binary distributions are available http://lucene.apache.org/solr/ --
see Resources > Downloads, and find a mirror near you.

In the below we are using Solr 3.3.0 on UNIX using .tgz packaging.  Use
unzip instead of tar if you are on Windows or use zip on UNIX.

Unpack Solr as follows:

  % tar zxvf apache-solr-3.3.0.tgz


Step 2.  Copy Kuromoji JARs to Apache Solr's lib directory
----------------------------------------------------------

Copy Kuromoji JARS to Solr's lib directory so they can be loaded by Solr
on startup.  In the below we are using the "example" Solr setup.

  % mkdir apache-solr-3.3.0/example/solr/lib # Unless it exists already
  % cp lib/kuromoji* apache-solr-3.3.0/example/solr/lib

The latter step should copy two JAR files to Solr's lib directory


Step 3.  Configure Apache Solr's schema to use Kuromoji
-------------------------------------------------------

Edit the "example" Solr schema.xml file to define a new field type for
Japanese text.  These are the fields that will be analyzed by Kuromoji.

Edit file

  apache-solr-3.3.0/example/solr/conf/schema.xml

and then add these lines within inside the <types> tag

  <fieldType name="text_ja" class="solr.TextField">
    <analyzer>
      <tokenizer class="org.atilika.kuromoji.solr.KuromojiTokenizerFactory"
                 mode="search"
                 user-dictionary=""/>
    </analyzer>
  </fieldType>

A user dictionary file to do custom tokenization can be provided using
the user-dictionary attribute.  Please see the Kuromoji documentation
for more information on this file format.

Mode "search" is the recommendedt mode for search, but "extended" is
also possibly.  The "extended" mode unigrams so-called unknown words
and unifies morphological analysis with n-gramming.  Please see the
documentation for further details.  There's also a mode "normal", but
we do not recommend it for search.

Define your own fields in schema.xml and use type="text_japanese" to
have Kuromoji to morphological analysis on these fields.

For example

 <field name="body" type="text_ja" indexed="true" stored="true"/>


Step 4.  Start Apache Solr
--------------------------

Start the "example" Solr using

  % cd apache-solr-3.3.0/example/
  % java -jar start.jar

Apache Solr will now start and have Kuromoji integrated
