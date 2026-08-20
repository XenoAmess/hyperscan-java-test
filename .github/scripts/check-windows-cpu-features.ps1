param(
    [Parameter(Mandatory = $true)]
    [string]$Platform
)

$ErrorActionPreference = 'Stop'

Add-Type -TypeDefinition @'
using System.Runtime.InteropServices;

public static class WindowsCpuFeatures
{
    [DllImport("kernel32.dll")]
    [return: MarshalAs(UnmanagedType.Bool)]
    public static extern bool IsProcessorFeaturePresent(uint processorFeature);
}
'@

$featureIds = [ordered]@{
    ssse3  = 36
    sse4_1 = 37
    sse4_2 = 38
    avx    = 39
    avx2   = 40
}

$supported = @(
    foreach ($feature in $featureIds.GetEnumerator()) {
        if ([WindowsCpuFeatures]::IsProcessorFeaturePresent($feature.Value)) {
            $feature.Key
        }
    }
)

$required = switch ($Platform) {
    'windows-x86_64-baseline' { @('ssse3', 'sse4_1', 'sse4_2'); break }
    'windows-x86_64' { @('ssse3', 'sse4_1', 'sse4_2', 'avx', 'avx2'); break }
    default { throw "Unsupported Windows platform: $Platform" }
}

$missing = @($required | Where-Object { $supported -notcontains $_ })
"CPU_FLAGS=$($supported -join ' ')" | Out-File -FilePath $env:GITHUB_ENV -Append -Encoding utf8

if ($missing.Count -gt 0) {
    $joined = $missing -join ','
    'SKIPPED=true' | Out-File -FilePath $env:GITHUB_OUTPUT -Append -Encoding utf8
    "MISSING_FEATURE=$joined" | Out-File -FilePath $env:GITHUB_OUTPUT -Append -Encoding utf8
    Write-Warning "CPU does not support $Platform; missing: $joined"
} else {
    'SKIPPED=false' | Out-File -FilePath $env:GITHUB_OUTPUT -Append -Encoding utf8
}
