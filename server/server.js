const express = require('express');
const { createClient } = require('@supabase/supabase-js');

const app = express();
const port = 3000;


const supabase = createClient(
  'https://dtxzuqapniscqgvkynbf.supabase.co',
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImR0eHp1cWFwbmlzY3Fndmt5bmJmIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczMzc3MzQyOCwiZXhwIjoyMDQ5MzQ5NDI4fQ.Ve4PT3n0wuDwzgJZFKyt7vfljK8o1J3krpnFxJNiGy4'
);


app.get('/generate-signed-url', async (req, res) => {
  const { bucketName, fileName } = req.query;

  if (!bucketName || !fileName) {
    return res.status(400).json({ error: 'Bucket name and file name are required' });
  }

  try {

    const { data, error } = await supabase
      .storage
      .from(bucketName)
      .createSignedUrl(fileName, 60 * 60);

    if (error) {
      throw error;
    }

    res.json({ signedUrl: data.signedUrl });
  } catch (error) {
    console.error('Error generating signed URL:', error);
    res.status(500).json({ error: 'Failed to generate signed URL', details: error.message });
  }
});


app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
